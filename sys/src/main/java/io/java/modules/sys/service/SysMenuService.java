package io.java.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.java.modules.sys.entity.SysMenuEntity;

import java.util.List;
/**
 * Menu management
 *
 * @author Jiasi Lu
 */
public interface SysMenuService extends IService<SysMenuEntity> {

	/**
	 * Query submenus according to parent menu
	 * @param parentId parent menu ID
	 * @param menuIdList user menu ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * Query submenus according to parent menu
	 * @param parentId parent menu ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);

	/**
	 * Get a menu list that does not contain buttons
	 */
	List<SysMenuEntity> queryNotButtonList();

	/**
	 * Get a user menu list
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);

	/**
	 *Delete
	 */
	void delete(Long menuId);
}
