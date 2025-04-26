package io.java.modules.sys.controller;

import io.java.common.annotation.SysLog;
import io.java.common.exception.RRException;
import io.java.common.utils.Constant;
import io.java.common.utils.R;
import io.java.modules.sys.entity.SysMenuEntity;
import io.java.modules.sys.service.ShiroService;
import io.java.modules.sys.service.SysMenuService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Collections;


/**
 * System Menu
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;

	/**
	 * Navigation menu
	 */
	@GetMapping("/nav")
	public R nav(){
		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
		Set<String> permissions = shiroService.getUserPermissions(getUserId());
		return R.ok().put("menuList", menuList).put("permissions", permissions);
	}
	
	/**
	 * List of all menus
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public List<SysMenuEntity> list(){
		List<SysMenuEntity> menuList = sysMenuService.list();

		//Query completed, sort this list directly
		Collections.sort(menuList);

		HashMap<Long, SysMenuEntity> menuMap = new HashMap<>(12);
		for (SysMenuEntity s : menuList) {
			menuMap.put(s.getMenuId(), s);
		}
		for (SysMenuEntity s : menuList) {
			SysMenuEntity parent = menuMap.get(s.getParentId());
			if (Objects.nonNull(parent)) {
				s.setParentName(parent.getName());
			}

		}


		return menuList;
	}
	
	/**
	 * Select menu (Add, Edit menu)
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select(){
		//Query list data
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

		//Add top level menu
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("First Menu");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);
		
		return R.ok().put("menuList", menuList);
	}
	
	/**
	 * Menu information
	 */
	@GetMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public R info(@PathVariable("menuId") Long menuId){
		SysMenuEntity menu = sysMenuService.getById(menuId);
		return R.ok().put("menu", menu);
	}
	
	/**
	 * Save
	 */
	@SysLog("Save Menu")
	@PostMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public R save(@RequestBody SysMenuEntity menu){
		//Data verification
		verifyForm(menu);
		
		sysMenuService.save(menu);
		
		return R.ok();
	}
	
	/**
	 * Edit
	 */
	@SysLog("Edit Menu")
	@PostMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public R update(@RequestBody SysMenuEntity menu){
		//Data verification
		verifyForm(menu);
				
		sysMenuService.updateById(menu);
		
		return R.ok();
	}
	
	/**
	 * Delete
	 */
	@SysLog("Delete Menu")
	@PostMapping("/delete/{menuId}")
	@RequiresPermissions("sys:menu:delete")
	public R delete(@PathVariable("menuId") long menuId){
		if(menuId <= 31){
			return R.error("System menu, cannot be deleted");
		}

		// Determine whether there is a submenu or button
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
		if(menuList.size() > 0){
			return R.error("Please Delete the submenu or button first");
		}

		sysMenuService.delete(menuId);

		return R.ok();
	}
	
	/**
	 * Verify that the parameters are correct
	 */
	private void verifyForm(SysMenuEntity menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new RRException("Menu name cannot be empty");
		}
		
		if(menu.getParentId() == null){
			throw new RRException("The parent menu cannot be empty");
		}
		
		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new RRException("Menu URL cannot be empty");
			}
		}

		//Previous menu type
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			SysMenuEntity parentMenu = sysMenuService.getById(menu.getParentId());
			parentType = parentMenu.getType();
		}

		//Directory, menu
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new RRException("The parent menu can only be of directory type");
			}
			return ;
		}

		//Button
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new RRException("The parent menu can only be of menu type");
			}
			return ;
		}
	}
}
