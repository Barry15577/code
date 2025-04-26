package io.java.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.java.common.validator.group.AddGroup;
import io.java.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * System users
 *
 * @author Jiasi Lu
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * User ID
	 */
	@TableId
	private Long userId;

	/**
	 * User Name
	 */
	@NotBlank(message="Username cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
	private String username;

	/**
	 * Password
	 */
	@NotBlank(message="Password cannot be empty", groups = AddGroup.class)
	private String password;

	/**
	 * salt
	 */
	private String salt;

	/**
	 * Email
	 */
	@NotBlank(message="Email cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
	@Email(message="The email format is incorrect", groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	/**
	 * Phone number
	 */
	private String mobile;

	/**
	 * Status 0: Disabled 1: Normal
	 */
	private Integer status;

	/**
	 * Role ID list
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;

	/**
	 * Creator ID
	 */
	private Long createUserId;

	/**
	 * Creation time
	 */
	private Date createTime;

}
