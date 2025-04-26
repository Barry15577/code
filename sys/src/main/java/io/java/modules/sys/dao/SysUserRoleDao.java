package io.java.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * Correspondence between users and roles
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {

	/**
	 * Get the role ID list based on the user ID
	 */
	List<Long> queryRoleIdList(Long userId);
	/**
	 * Batch Delete based on role ID array
	 */
	int deleteBatch(Long[] roleIds);
}
