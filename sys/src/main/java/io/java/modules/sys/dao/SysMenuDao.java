package io.java.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Menu management
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

	/**
	 * Query submenus based on parent menu
	 * @param parentId parent menu ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);

	/**
	 * Get the menu list without buttons
	 */
	List<SysMenuEntity> queryNotButtonList();

}
