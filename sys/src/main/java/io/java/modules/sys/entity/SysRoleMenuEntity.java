
package io.java.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Correspondence between roles and menus
 *
 * @author Jiasi Lu
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	/**
	 * Role ID
	 */
	private Long roleId;

	/**
	 * Menu ID
	 */
	private Long menuId;
}
