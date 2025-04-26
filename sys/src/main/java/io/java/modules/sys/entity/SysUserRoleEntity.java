

package io.java.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * Correspondence between users and roles
 *
 * @author Jiasi Lu
 */
@Data
@TableName("sys_user_role")
public class SysUserRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private Long id;

	/**
	 * User ID
	 */
	private Long userId;

	/**
	 * Role ID
	 */
	private Long roleId;
}
