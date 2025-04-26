package io.java.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * System log
 *
 * @author Jiasi Lu
 */
@Data
@TableName("sys_log")
public class SysLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private Long id;
	//User name
	private String username;
	//User operation
	private String operation;
	//Request method
	private String method;
	//Request parameters
	private String params;
	//Execution time (milliseconds)
	private Long time;
	//IP address
	private String ip;
	//Creation time
	private Date createDate;

}
