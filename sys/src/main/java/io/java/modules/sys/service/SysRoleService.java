package io.java.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;
/**
 * Role
 *
 * @author Jiasi Lu
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveRole(SysRoleEntity role);

	void update(SysRoleEntity role);

	void deleteBatch(Long[] roleIds);

	/**
	 * Query the role ID list created by the user
	 */
	List<Long> queryRoleIdList(Long createUserId);
}