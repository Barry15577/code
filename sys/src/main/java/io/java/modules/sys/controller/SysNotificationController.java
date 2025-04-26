package io.java.modules.sys.controller;

import io.java.common.annotation.SysLog;
import io.java.common.utils.PageUtils;
import io.java.common.utils.R;
import io.java.modules.sys.entity.SysNotificationEntity;
import io.java.modules.sys.service.SysNotificationService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * System Notification Controller
 * Handles HTTP requests for system notifications
 *
 * @author Jiasi Lu
 */
@RestController
@RequestMapping("/sys/notification")
public class SysNotificationController {
    @Autowired
    private SysNotificationService sysNotificationService;
    
    /**
     * List notifications with pagination and filters
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:notification:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysNotificationService.queryPage(params);
        
        return R.ok().put("page", page);
    }
    
    /**
     * Get notification details by ID
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:notification:info")
    public R info(@PathVariable("id") Long id) {
        SysNotificationEntity notification = sysNotificationService.getNotificationById(id);
        
        return R.ok().put("notification", notification);
    }
    
    /**
     * Save a new notification
     */
    @SysLog("Save Notification")
    @PostMapping("/save")
    @RequiresPermissions("sys:notification:save")
    public R save(@RequestBody SysNotificationEntity notification) {
        // Set created by
        notification.setCreatedBy(SecurityUtils.getSubject().getPrincipal().toString());
        
        sysNotificationService.saveNotification(notification);
        
        return R.ok();
    }
    
    /**
     * Update an existing notification
     */
    @SysLog("Update Notification")
    @PostMapping("/update")
    @RequiresPermissions("sys:notification:update")
    public R update(@RequestBody SysNotificationEntity notification) {
        sysNotificationService.updateNotification(notification);
        
        return R.ok();
    }
    
    /**
     * Delete a notification
     */
    @SysLog("Delete Notification")
    @PostMapping("/delete")
    @RequiresPermissions("sys:notification:delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            sysNotificationService.deleteNotification(id);
        }
        
        return R.ok();
    }
    
    /**
     * Publish a notification
     */
    @SysLog("Publish Notification")
    @PostMapping("/publish/{id}")
    @RequiresPermissions("sys:notification:update")
    public R publish(@PathVariable("id") Long id) {
        sysNotificationService.publishNotification(id);
        
        return R.ok();
    }
    
    /**
     * Unpublish a notification
     */
    @SysLog("Unpublish Notification")
    @PostMapping("/unpublish/{id}")
    @RequiresPermissions("sys:notification:update")
    public R unpublish(@PathVariable("id") Long id) {
        sysNotificationService.unpublishNotification(id);
        
        return R.ok();
    }
    
    /**
     * Get active notifications for current user
     * No permission required as this is used to display notifications to all users
     */
    @GetMapping("/active")
    public R getActiveNotifications() {
        // Get current user role
        String userType = "USER"; // Default to regular user
        
        // Check if the user is an admin
        boolean isAdmin = SecurityUtils.getSubject().hasRole("admin");
        if (isAdmin) {
            userType = "ADMIN";
        }
        
        List<SysNotificationEntity> notifications = sysNotificationService.getActiveNotifications(userType);
        
        return R.ok().put("notifications", notifications);
    }
    
    /**
     * Mark notifications as read for the current user
     * No permission required as this is used by all users
     */
    @PostMapping("/markAsRead")
    public R markNotificationsAsRead(@RequestBody Long[] ids) {
        // Get current user ID
        String userId = SecurityUtils.getSubject().getPrincipal().toString();
        
        if (ids != null && ids.length > 0) {
            sysNotificationService.markNotificationsAsRead(userId, ids);
        }
        
        return R.ok();
    }
} 