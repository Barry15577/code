package io.java.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.modules.sys.entity.SysUserRoleEntity;

import java.util.List;
/**
 * Correspondence between users and roles
 *
 * @author Jiasi Lu
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {

	void saveOrUpdate(Long userId, List<Long> roleIdList);

	/**
	 * Get the role ID list based on the user ID
	 */
	List<Long> queryRoleIdList(Long userId);

	/**
	 * Batch Delete based on the role ID array
	 */
	int deleteBatch(Long[] roleIds);
}
