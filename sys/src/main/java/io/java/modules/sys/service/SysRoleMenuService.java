package io.java.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.modules.sys.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * Correspondence between roles and menus
 *
 * @author Jiasi Lu
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {

	void saveOrUpdate(Long roleId, List<Long> menuIdList);

	/**
	 * Get the menu ID list based on the role ID
	 */
	List<Long> queryMenuIdList(Long roleId);

	/**
	 * Batch Delete based on the role ID array
	 */
	int deleteBatch(Long[] roleIds);

}