package io.java.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu management
 *
 * @author Jiasi Lu
 */
@Data
@TableName("sys_menu")
public class SysMenuEntity implements Serializable,Comparable<SysMenuEntity> {
	private static final long serialVersionUID = 1L;

	/**
	 * Menu ID
	 */
	@TableId
	private Long menuId;

	/**
	 * Parent menu ID, First Menu is 0
	 */
	private Long parentId;

	/**
	 * Parent menu name
	 */
	@TableField(exist=false)
	private String parentName;

	/**
	 * Menu name
	 */
	private String name;

	/**
	 * Menu URL
	 */
	private String url;

	/**
	 * Authorization (multiple separated by commas, such as: user:list, user:create)
	 */
	private String perms;

	/**
	 * Type 0: Directory 1: Menu 2: Button
	 */
	private Integer type;

	/**
	 * Menu icon
	 */
	private String icon;

	/**
	 * Sorting
	 */
	private Integer orderNum;

	/**
	 * ztree attribute
	 */
	@TableField(exist=false)
	private Boolean open;

	@TableField(exist=false)
	private List<SysMenuEntity> list=new ArrayList<>();

	@Override
	public int compareTo(SysMenuEntity o) {
		return this.getOrderNum()-o.getOrderNum();
	}
}
