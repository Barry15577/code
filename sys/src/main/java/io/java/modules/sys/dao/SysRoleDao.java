

package io.java.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Role Management
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {
	
	/**
	 * Query the role ID list created by the user
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
