package io.java.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;
/**
 * System user
 *
 * @author Jiasi Lu
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * Query all user permissions
	 * @param userId user ID
	 */
	List<String> queryAllPerms(Long userId);

	/**
	 * Query all user menu IDs
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * Query system users by username
	 */
	SysUserEntity queryByUserName(String username);

	/**
	 * Save user
	 */
	void saveUser(SysUserEntity user);

	/**
	 * Edit user
	 */
	void update(SysUserEntity user);

	/**
	 * Delete user
	 */
	void deleteBatch(Long[] userIds);

	/**
	 * Edit password
	 * @param userId User ID
	 * @param password Original password
	 * @param newPassword New password
	 */
	boolean updatePassword(Long userId, String password, String newPassword);
}
