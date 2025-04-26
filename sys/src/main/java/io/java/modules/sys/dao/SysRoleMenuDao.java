
package io.java.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Correspondence between roles and menus
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {
	
	/**
	 * Get the menu ID list based on the role ID
	 */
	List<Long> queryMenuIdList(Long roleId);

	/**
	 * Batch Delete based on role ID array
	 */
	int deleteBatch(Long[] roleIds);
}
