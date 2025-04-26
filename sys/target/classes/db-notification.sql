-- Create notification table
CREATE TABLE `sys_notification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Notification ID',
  `title` varchar(100) NOT NULL COMMENT 'Notification title',
  `content` text COMMENT 'Notification content',
  `type` varchar(20) DEFAULT 'INFO' COMMENT 'Notification type: INFO, WARNING, ERROR, SUCCESS',
  `priority` varchar(10) DEFAULT 'MEDIUM' COMMENT 'Priority: LOW, MEDIUM, HIGH',
  `target` varchar(10) DEFAULT 'ALL' COMMENT 'Target audience: ALL, ADMIN, USER',
  `status` tinyint(4) DEFAULT '0' COMMENT 'Status: 0-Disabled, 1-Enabled',
  `publish_time` datetime DEFAULT NULL COMMENT 'Publish time',
  `expiration_time` datetime DEFAULT NULL COMMENT 'Expiration time',
  `created_by` varchar(50) DEFAULT NULL COMMENT 'Created by',
  `create_time` datetime DEFAULT NULL COMMENT 'Create time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_target` (`target`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_expiration_time` (`expiration_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Notification Table';

-- Create user notification read table to track which notifications have been read
CREATE TABLE `sys_user_notification_read` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` varchar(50) NOT NULL COMMENT 'User ID',
  `notification_id` bigint(20) NOT NULL COMMENT 'Notification ID',
  `read_time` datetime NOT NULL COMMENT 'Time when notification was read',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_notification` (`user_id`,`notification_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_notification_id` (`notification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User Notification Read Tracking Table';

-- Add menu items for Notification Management
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1501, 1, 'Notification Management', 'modules/sys/notification', NULL, 1, 'icon-notification', 5);

-- Add permissions for notification actions
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1502, 1501, 'View', NULL, 'sys:notification:list,sys:notification:info', 2, NULL, 0);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1503, 1501, 'Add', NULL, 'sys:notification:save', 2, NULL, 1);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1504, 1501, 'Edit', NULL, 'sys:notification:update', 2, NULL, 2);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1505, 1501, 'Delete', NULL, 'sys:notification:delete', 2, NULL, 3);

-- Add menu item for Notification Dashboard
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1506, 1501, 'Dashboard', 'modules/sys/notification-dashboard', 'sys:notification:list', 2, 'icon-chart', 4); 