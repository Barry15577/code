package io.java.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.sys.entity.SysNotificationEntity;

import java.util.List;
import java.util.Map;

/**
 * System Notification Service
 * Provides methods for managing system notifications
 *
 * @author Jiasi Lu
 */
public interface SysNotificationService extends IService<SysNotificationEntity> {

    /**
     * Get a paginated list of notifications
     *
     * @param params Query parameters including page, limit, and filters
     * @return Paginated notification list
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * Get a notification by ID
     *
     * @param id Notification ID
     * @return Notification entity
     */
    SysNotificationEntity getNotificationById(Long id);

    /**
     * Save a new notification
     *
     * @param notification Notification entity to save
     */
    void saveNotification(SysNotificationEntity notification);

    /**
     * Update an existing notification
     *
     * @param notification Notification entity with updated data
     */
    void updateNotification(SysNotificationEntity notification);

    /**
     * Delete a notification by ID
     *
     * @param id Notification ID to delete
     */
    void deleteNotification(Long id);

    /**
     * Publish a notification (set status to enabled and publish time)
     *
     * @param id Notification ID to publish
     */
    void publishNotification(Long id);

    /**
     * Unpublish a notification (set status to disabled)
     *
     * @param id Notification ID to unpublish
     */
    void unpublishNotification(Long id);

    /**
     * Get active notifications for a specific target audience
     *
     * @param target Target audience (ALL, ADMIN, USER)
     * @return List of active notifications
     */
    List<SysNotificationEntity> getActiveNotifications(String target);

    /**
     * Mark notifications as read for a specific user
     *
     * @param userId User ID who read the notifications
     * @param notificationIds IDs of notifications to mark as read
     */
    void markNotificationsAsRead(String userId, Long[] notificationIds);

    /**
     * Get notification statistics by type
     *
     * @return Map containing notification counts by type
     */
    Map<String, Object> getNotificationStatsByType();

    /**
     * Get notification statistics by priority
     *
     * @return Map containing notification counts by priority
     */
    Map<String, Object> getNotificationStatsByPriority();

    /**
     * Get notification statistics by status
     *
     * @return Map containing notification counts by status
     */
    Map<String, Object> getNotificationStatsByStatus();

    /**
     * Get notification statistics by target audience
     *
     * @return Map containing notification counts by target audience
     */
    Map<String, Object> getNotificationStatsByTarget();

    /**
     * Get notification statistics for a time period
     *
     * @param days Number of days to include in the statistics
     * @return Map containing time series data for notifications
     */
    Map<String, Object> getNotificationStatsByTimePeriod(int days);

    /**
     * Get notification read statistics
     *
     * @return Map containing read/unread statistics for notifications
     */
    Map<String, Object> getNotificationReadStats();
} 