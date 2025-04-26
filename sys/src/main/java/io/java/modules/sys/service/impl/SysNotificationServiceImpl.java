package io.java.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.PageUtils;
import io.java.common.utils.Query;
import io.java.modules.sys.entity.SysNotificationEntity;
import io.java.modules.sys.entity.SysUserNotificationReadEntity;
import io.java.modules.sys.mapper.SysNotificationMapper;
import io.java.modules.sys.mapper.SysUserNotificationReadMapper;
import io.java.modules.sys.service.SysNotificationService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * System Notification Service Implementation
 * Implements business operations for managing system notifications
 *
 * @author Jiasi Lu
 */
@Service("sysNotificationService")
public class SysNotificationServiceImpl extends ServiceImpl<SysNotificationMapper, SysNotificationEntity> implements SysNotificationService {

    @Autowired
    private SysUserNotificationReadMapper userNotificationReadDao;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        String type = (String) params.get("type");
        String priority = (String) params.get("priority");
        String target = (String) params.get("target");
        
        // Handle status parameter (can be Integer or String)
        Object statusObj = params.get("status");
        Integer status = null;
        
        if (statusObj != null) {
            if (statusObj instanceof Integer) {
                status = (Integer) statusObj;
            } else if (statusObj instanceof String && StringUtils.isNotBlank((String) statusObj)) {
                try {
                    status = Integer.parseInt((String) statusObj);
                } catch (NumberFormatException e) {
                    // If conversion fails, leave it as null
                }
            }
        }
        
        QueryWrapper<SysNotificationEntity> queryWrapper = new QueryWrapper<>();
        
        // Add filters if provided
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        
        if (StringUtils.isNotBlank(type)) {
            queryWrapper.eq("type", type);
        }
        
        if (StringUtils.isNotBlank(priority)) {
            queryWrapper.eq("priority", priority);
        }
        
        if (StringUtils.isNotBlank(target)) {
            queryWrapper.eq("target", target);
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // Sort by create time descending (newest first)
        queryWrapper.orderByDesc("create_time");
        
        IPage<SysNotificationEntity> page = this.page(
                new Query<SysNotificationEntity>().getPage(params),
                queryWrapper
        );
        
        return new PageUtils(page);
    }
    
    @Override
    public SysNotificationEntity getNotificationById(Long id) {
        return this.getById(id);
    }
    
    @Override
    public void saveNotification(SysNotificationEntity notification) {
        // Set default values
        if (notification.getStatus() == null) {
            notification.setStatus(0); // Default to disabled/draft
        }
        
        notification.setCreateTime(new Date());
        notification.setUpdateTime(new Date());
        
        this.save(notification);
    }
    
    @Override
    public void updateNotification(SysNotificationEntity notification) {
        notification.setUpdateTime(new Date());
        this.updateById(notification);
    }
    
    @Override
    public void deleteNotification(Long id) {
        this.removeById(id);
    }
    
    @Override
    public void publishNotification(Long id) {
        SysNotificationEntity notification = this.getById(id);
        if (notification != null) {
            notification.setStatus(1); // Set to enabled
            notification.setPublishTime(new Date());
            notification.setUpdateTime(new Date());
            this.updateById(notification);
        }
    }
    
    @Override
    public void unpublishNotification(Long id) {
        SysNotificationEntity notification = this.getById(id);
        if (notification != null) {
            notification.setStatus(0); // Set to disabled
            notification.setUpdateTime(new Date());
            this.updateById(notification);
        }
    }

    @Override
    public List<SysNotificationEntity> getActiveNotifications(String target) {
        QueryWrapper<SysNotificationEntity> queryWrapper = new QueryWrapper<>();
        
        // Get notifications that are:
        // 1. Enabled (status = 1)
        // 2. Either have no expiration or the expiration time is in the future
        // 3. Match the target audience or are for ALL users
        queryWrapper.eq("status", 1)
                .and(wrapper -> wrapper.isNull("expiration_time")
                        .or(w -> w.gt("expiration_time", new Date())))
                .and(wrapper -> wrapper.eq("target", target)
                        .or(w -> w.eq("target", "ALL")));
        
        // Sort by priority (HIGH first) and then creation date (newest first)
        queryWrapper.orderByDesc(
                "CASE " +
                "WHEN priority = 'HIGH' THEN 1 " +
                "WHEN priority = 'MEDIUM' THEN 2 " +
                "WHEN priority = 'LOW' THEN 3 " +
                "ELSE 4 END"
        ).orderByDesc("create_time");
        
        List<SysNotificationEntity> notifications = this.list(queryWrapper);
        
        // Get current user ID from SecurityUtils
        String userId = SecurityUtils.getSubject().getPrincipal().toString();
        
        // If we have a valid user, filter out notifications that they've already read
        if (StringUtils.isNotBlank(userId) && !notifications.isEmpty()) {
            // Get all notification IDs that the user has read
            QueryWrapper<SysUserNotificationReadEntity> readQueryWrapper = new QueryWrapper<>();
            readQueryWrapper.eq("user_id", userId)
                    .in("notification_id", 
                        notifications.stream()
                            .map(SysNotificationEntity::getId)
                            .collect(Collectors.toList()));
            
            List<SysUserNotificationReadEntity> readRecords = userNotificationReadDao.selectList(readQueryWrapper);
            
            // If the user has read some notifications, filter them out
            if (!readRecords.isEmpty()) {
                List<Long> readNotificationIds = readRecords.stream()
                        .map(SysUserNotificationReadEntity::getNotificationId)
                        .collect(Collectors.toList());
                
                // Keep only unread notifications
                notifications = notifications.stream()
                        .filter(notification -> !readNotificationIds.contains(notification.getId()))
                        .collect(Collectors.toList());
            }
        }
        
        return notifications;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markNotificationsAsRead(String userId, Long[] notificationIds) {
        if (userId == null || notificationIds == null || notificationIds.length == 0) {
            return;
        }
        
        // Check which notifications are already marked as read by this user
        QueryWrapper<SysUserNotificationReadEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .in("notification_id", Arrays.asList(notificationIds));
        List<SysUserNotificationReadEntity> existingRecords = userNotificationReadDao.selectList(queryWrapper);
        
        // Create a list of notification IDs that are already marked as read
        List<Long> alreadyReadIds = existingRecords.stream()
                .map(SysUserNotificationReadEntity::getNotificationId)
                .collect(Collectors.toList());
        
        // For each notification that hasn't been marked as read, create a new record
        Date now = new Date();
        for (Long notificationId : notificationIds) {
            if (!alreadyReadIds.contains(notificationId)) {
                SysUserNotificationReadEntity record = new SysUserNotificationReadEntity();
                record.setUserId(userId);
                record.setNotificationId(notificationId);
                record.setReadTime(now);
                userNotificationReadDao.insert(record);
            }
        }
    }
    
    @Override
    public Map<String, Object> getNotificationStatsByType() {
        // Get all notifications
        List<SysNotificationEntity> notifications = this.list();
        
        // Group notifications by type
        Map<String, Long> countByType = notifications.stream()
                .collect(Collectors.groupingBy(
                        notification -> StringUtils.isNotBlank(notification.getType()) ? notification.getType() : "UNKNOWN",
                        Collectors.counting()
                ));
        
        // Ensure all types are included with at least zero count
        if (!countByType.containsKey("INFO")) countByType.put("INFO", 0L);
        if (!countByType.containsKey("WARNING")) countByType.put("WARNING", 0L);
        if (!countByType.containsKey("ERROR")) countByType.put("ERROR", 0L);
        if (!countByType.containsKey("SUCCESS")) countByType.put("SUCCESS", 0L);
        
        // Prepare the result
        Map<String, Object> result = new HashMap<>();
        result.put("types", new ArrayList<>(countByType.keySet()));
        result.put("counts", new ArrayList<>(countByType.values()));
        
        return result;
    }
    
    @Override
    public Map<String, Object> getNotificationStatsByPriority() {
        // Get all notifications
        List<SysNotificationEntity> notifications = this.list();
        
        // Group notifications by priority
        Map<String, Long> countByPriority = notifications.stream()
                .collect(Collectors.groupingBy(
                        notification -> StringUtils.isNotBlank(notification.getPriority()) ? notification.getPriority() : "UNKNOWN",
                        Collectors.counting()
                ));
        
        // Ensure all priorities are included with at least zero count
        if (!countByPriority.containsKey("LOW")) countByPriority.put("LOW", 0L);
        if (!countByPriority.containsKey("MEDIUM")) countByPriority.put("MEDIUM", 0L);
        if (!countByPriority.containsKey("HIGH")) countByPriority.put("HIGH", 0L);
        
        // Prepare the result
        Map<String, Object> result = new HashMap<>();
        result.put("priorities", new ArrayList<>(countByPriority.keySet()));
        result.put("counts", new ArrayList<>(countByPriority.values()));
        
        return result;
    }
    
    @Override
    public Map<String, Object> getNotificationStatsByStatus() {
        // Get all notifications
        List<SysNotificationEntity> notifications = this.list();
        
        // Group notifications by status
        Map<String, Long> countByStatus = new HashMap<>();
        Long enabledCount = notifications.stream()
                .filter(notification -> notification.getStatus() != null && notification.getStatus() == 1)
                .count();
        Long disabledCount = notifications.stream()
                .filter(notification -> notification.getStatus() == null || notification.getStatus() == 0)
                .count();
        
        countByStatus.put("Enabled", enabledCount);
        countByStatus.put("Disabled", disabledCount);
        
        // Prepare the result
        Map<String, Object> result = new HashMap<>();
        result.put("statuses", new ArrayList<>(countByStatus.keySet()));
        result.put("counts", new ArrayList<>(countByStatus.values()));
        
        return result;
    }
    
    @Override
    public Map<String, Object> getNotificationStatsByTarget() {
        // Get all notifications
        List<SysNotificationEntity> notifications = this.list();
        
        // Group notifications by target
        Map<String, Long> countByTarget = notifications.stream()
                .collect(Collectors.groupingBy(
                        notification -> StringUtils.isNotBlank(notification.getTarget()) ? notification.getTarget() : "UNKNOWN",
                        Collectors.counting()
                ));
        
        // Ensure all targets are included with at least zero count
        if (!countByTarget.containsKey("ALL")) countByTarget.put("ALL", 0L);
        if (!countByTarget.containsKey("ADMIN")) countByTarget.put("ADMIN", 0L);
        if (!countByTarget.containsKey("USER")) countByTarget.put("USER", 0L);
        
        // Prepare the result
        Map<String, Object> result = new HashMap<>();
        result.put("targets", new ArrayList<>(countByTarget.keySet()));
        result.put("counts", new ArrayList<>(countByTarget.values()));
        
        return result;
    }
    
    @Override
    public Map<String, Object> getNotificationStatsByTimePeriod(int days) {
        // Setup time range
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days + 1); // +1 to include today
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        
        // Get all notifications within the time range
        QueryWrapper<SysNotificationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("create_time", startDate);
        List<SysNotificationEntity> notifications = this.list(queryWrapper);
        
        // Format for daily statistics
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Initialize the date range with zeros
        Map<String, Long> dailyCounts = new LinkedHashMap<>();
        for (int i = 0; i < days; i++) {
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dailyCounts.put(dateFormat.format(calendar.getTime()), 0L);
        }
        
        // Group notifications by creation date
        Map<String, Long> notificationsByDate = notifications.stream()
                .collect(Collectors.groupingBy(
                        notification -> dateFormat.format(notification.getCreateTime()),
                        Collectors.counting()
                ));
        
        // Merge the counts
        notificationsByDate.forEach(dailyCounts::put);
        
        // Prepare the result
        Map<String, Object> result = new HashMap<>();
        result.put("dates", new ArrayList<>(dailyCounts.keySet()));
        result.put("counts", new ArrayList<>(dailyCounts.values()));
        
        return result;
    }
    
    @Override
    public Map<String, Object> getNotificationReadStats() {
        // Get all notifications
        List<SysNotificationEntity> notifications = this.list();
        
        // Get the count of notifications
        long totalCount = notifications.size();
        
        // For each notification, count how many times it has been read
        Map<Long, Long> readCountByNotificationId = new HashMap<>();
        
        for (SysNotificationEntity notification : notifications) {
            QueryWrapper<SysUserNotificationReadEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("notification_id", notification.getId());
            long readCount = userNotificationReadDao.selectCount(queryWrapper);
            readCountByNotificationId.put(notification.getId(), readCount);
        }
        
        // Count notifications with zero reads
        long unreadCount = readCountByNotificationId.values().stream()
                .filter(count -> count == 0)
                .count();
        
        // Count notifications with at least one read
        long readCount = totalCount - unreadCount;
        
        // Prepare the result
        Map<String, Object> result = new HashMap<>();
        result.put("statuses", Arrays.asList("Read", "Unread"));
        result.put("counts", Arrays.asList(readCount, unreadCount));
        
        // Add detailed read statistics
        Map<String, Long> readStatsMap = new HashMap<>();
        readStatsMap.put("total", totalCount);
        readStatsMap.put("read", readCount);
        readStatsMap.put("unread", unreadCount);
        
        double readRatio = totalCount > 0 ? ((double) readCount / totalCount) * 100 : 0;
        readStatsMap.put("readPercentage", Math.round(readRatio * 100) / 100L); // Round to 2 decimal places
        
        result.put("stats", readStatsMap);
        
        return result;
    }
}