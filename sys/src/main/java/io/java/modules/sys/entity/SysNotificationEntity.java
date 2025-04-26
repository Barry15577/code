package io.java.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * System Notification Entity
 * Represents a system-wide notification that can be viewed by users
 *
 * @author Jiasi Lu
 */
@Data
@TableName("sys_notification")
public class SysNotificationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Notification ID
     */
    @TableId
    private Long id;

    /**
     * Notification title
     */
    private String title;

    /**
     * Notification content
     */
    private String content;

    /**
     * Notification type (INFO, WARNING, ERROR, SUCCESS)
     */
    private String type;

    /**
     * Notification priority (LOW, MEDIUM, HIGH)
     */
    private String priority;

    /**
     * Target audience (ALL, ADMIN, USER)
     */
    private String target;

    /**
     * Status (0: Disabled, 1: Enabled)
     */
    private Integer status;

    /**
     * Publish date/time
     */
    private Date publishTime;

    /**
     * Expiration date/time
     */
    private Date expirationTime;

    /**
     * Created by
     */
    private String createdBy;

    /**
     * Create time
     */
    private Date createTime;

    /**
     * Update time
     */
    private Date updateTime;
} 
