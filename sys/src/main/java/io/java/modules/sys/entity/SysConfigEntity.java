

package io.java.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * System configuration information
 *
 * @author Jiasi Lu
 */
@Data
@TableName("sys_config")
public class SysConfigEntity {
	@TableId
	private Long id;
	@NotBlank(message="Parameter name cannot be empty")
	private String paramKey;
	@NotBlank(message="Parameter value cannot be empty")
	private String paramValue;
	private String remark;

}
