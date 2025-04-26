

package io.java.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * System user Token
 *
 * @author Jiasi Lu
 */
@Data
@TableName("sys_user_token")
public class SysUserTokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//User ID
	@TableId(type = IdType.INPUT)
	private Long userId;
	//token
	private String token;
	//Expiration time
	private Date expireTime;
	//Update time
	private Date updateTime;

}
