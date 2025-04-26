package io.java.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.Constant;
import io.java.common.utils.MapUtils;
import io.java.modules.sys.dao.SysMenuDao;
import io.java.modules.sys.entity.SysMenuEntity;
import io.java.modules.sys.service.SysMenuService;
import io.java.modules.sys.service.SysRoleMenuService;
import io.java.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return baseMapper.queryNotButtonList();
	}

	@Override
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		//System administrator, has the highest authority
		if(userId == Constant.SUPER_ADMIN){
			return getMenuList(null);
		}

		//User menu list
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getMenuList(menuIdList);
	}

	/**
	 * Get the menu list
	 * @param menuIdList
	 * @return
	 */
	private List<SysMenuEntity> getMenuList(List<Long> menuIdList) {
         // Query all menus
		List<SysMenuEntity> menus = this.baseMapper.selectList(new QueryWrapper<SysMenuEntity>()
				.in(Objects.nonNull(menuIdList), "menu_id", menuIdList).in("type", 0, 1));
         // Query completed. Sort this list directly
		Collections.sort(menus);

        // Bind id and menu
		HashMap<Long, SysMenuEntity> menuMap = new HashMap<>(12);
		for (SysMenuEntity s : menus) {
			menuMap.put(s.getMenuId(), s);
		}
        // Use iterators to assemble the hierarchical relationship of menus
		Iterator<SysMenuEntity> iterator = menus.iterator();
		while (iterator.hasNext()) {
			SysMenuEntity menu = iterator.next();
			SysMenuEntity parent = menuMap.get(menu.getParentId());
			if (Objects.nonNull(parent)) {
				parent.getList().add(menu);
         // Remove this menu from the current node
				iterator.remove();
			}
		}

		return menus;
	}

	@Override
	public void delete(Long menuId){
//Delete menu
		this.removeById(menuId);
//Delete menu and role association
		sysRoleMenuService.removeByMap(new MapUtils().put("menu_id", menuId));
	}

	/**
	 * Get all menu lists
	 */
	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
//Query root menu list
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
//Recursively get submenus
		getMenuTreeList(menuList, menuIdList);

		return menuList;
	}

	/**
	 * recursion
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();

		for(SysMenuEntity entity : menuList){
//Table of contents
			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}

		return subMenuList;
	}
}
