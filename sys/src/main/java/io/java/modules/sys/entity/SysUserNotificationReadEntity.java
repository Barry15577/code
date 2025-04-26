package io.java.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * System User Notification Read Entity
 * Tracks which notifications have been read by which users
 *
 * @author Jiasi Lu
 */
@Data
@TableName("sys_user_notification_read")
public class SysUserNotificationReadEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * User ID
     */
    private String userId;

    /**
     * Notification ID
     */
    private Long notificationId;

    /**
     * Read time
     */
    private Date readTime;
} 