package io.java.modules.sys.controller;

import io.java.common.utils.R;
import io.java.modules.sys.service.SysNotificationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Notification Dashboard Controller
 * Provides data for the notification statistics dashboard
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/sys/dashboard/notification")
public class NotificationDashboardController {

    @Autowired
    private SysNotificationService notificationService;

    /**
     * Get notification statistics by type
     */
    @GetMapping("/stats/by-type")
    @RequiresPermissions("sys:notification:list")
    public R getNotificationStatsByType() {
        Map<String, Object> result = notificationService.getNotificationStatsByType();
        return R.ok().put("data", result);
    }

    /**
     * Get notification statistics by priority
     */
    @GetMapping("/stats/by-priority")
    @RequiresPermissions("sys:notification:list")
    public R getNotificationStatsByPriority() {
        Map<String, Object> result = notificationService.getNotificationStatsByPriority();
        return R.ok().put("data", result);
    }

    /**
     * Get notification statistics by status
     */
    @GetMapping("/stats/by-status")
    @RequiresPermissions("sys:notification:list")
    public R getNotificationStatsByStatus() {
        Map<String, Object> result = notificationService.getNotificationStatsByStatus();
        return R.ok().put("data", result);
    }

    /**
     * Get notification statistics by target audience
     */
    @GetMapping("/stats/by-target")
    @RequiresPermissions("sys:notification:list")
    public R getNotificationStatsByTarget() {
        Map<String, Object> result = notificationService.getNotificationStatsByTarget();
        return R.ok().put("data", result);
    }

    /**
     * Get notification statistics by time period (last 7 days)
     */
    @GetMapping("/stats/by-time")
    @RequiresPermissions("sys:notification:list")
    public R getNotificationStatsByTime() {
        Map<String, Object> result = notificationService.getNotificationStatsByTimePeriod(7);
        return R.ok().put("data", result);
    }

    /**
     * Get notification read statistics
     */
    @GetMapping("/stats/read-ratio")
    @RequiresPermissions("sys:notification:list")
    public R getNotificationReadStats() {
        Map<String, Object> result = notificationService.getNotificationReadStats();
        return R.ok().put("data", result);
    }

    /**
     * Get all notification dashboard stats in one call
     */
    @GetMapping("/stats/all")
    @RequiresPermissions("sys:notification:list")
    public R getAllNotificationStats() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("byType", notificationService.getNotificationStatsByType());
        result.put("byPriority", notificationService.getNotificationStatsByPriority());
        result.put("byStatus", notificationService.getNotificationStatsByStatus());
        result.put("byTarget", notificationService.getNotificationStatsByTarget());
        result.put("byTime", notificationService.getNotificationStatsByTimePeriod(7));
        result.put("readRatio", notificationService.getNotificationReadStats());
        
        return R.ok().put("data", result);
    }
} 