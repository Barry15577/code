package io.java.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * System users
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

	/**
	 * Query all permissions of the user
	 * @param userId user ID
	 */
	List<String> queryAllPerms(Long userId);

	/**
	 * Query all menu IDs of the user
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * Query system users based on user name
	 */
	SysUserEntity queryByUserName(String username);

}
