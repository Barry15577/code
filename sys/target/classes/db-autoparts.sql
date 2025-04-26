-- Create auto parts table
CREATE TABLE `crm_auto_part` (
  `part_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Part ID',
  `part_number` varchar(50) NOT NULL COMMENT 'Part number',
  `part_name` varchar(100) NOT NULL COMMENT 'Part name',
  `category` varchar(50) DEFAULT NULL COMMENT 'Part category',
  `subcategory` varchar(50) DEFAULT NULL COMMENT 'Part subcategory',
  `manufacturer` varchar(100) DEFAULT NULL COMMENT 'Manufacturer name',
  `compatible_makes` varchar(255) DEFAULT NULL COMMENT 'Compatible car makes',
  `compatible_models` varchar(255) DEFAULT NULL COMMENT 'Compatible car models',
  `year_range` varchar(50) DEFAULT NULL COMMENT 'Compatible year range',
  `stock_quantity` int(11) DEFAULT '0' COMMENT 'Current stock quantity',
  `low_stock_threshold` int(11) DEFAULT '5' COMMENT 'Low stock threshold',
  `purchase_price` decimal(10,2) DEFAULT NULL COMMENT 'Purchase price',
  `selling_price` decimal(10,2) DEFAULT NULL COMMENT 'Selling price',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT 'Supplier ID',
  `supplier_name` varchar(100) DEFAULT NULL COMMENT 'Supplier name',
  `location` varchar(100) DEFAULT NULL COMMENT 'Location in warehouse',
  `dimensions` varchar(100) DEFAULT NULL COMMENT 'Product dimensions',
  `weight` decimal(10,2) DEFAULT NULL COMMENT 'Product weight',
  `weight_unit` varchar(10) DEFAULT NULL COMMENT 'Weight unit',
  `condition` varchar(20) DEFAULT 'New' COMMENT 'Part condition',
  `oem_number` varchar(50) DEFAULT NULL COMMENT 'OEM number',
  `description` text COMMENT 'Part description',
  `image_url` varchar(255) DEFAULT NULL COMMENT 'Image URL',
  `status` tinyint(4) DEFAULT '1' COMMENT 'Status: 0-Inactive, 1-Active',
  `restock_date` datetime DEFAULT NULL COMMENT 'Last restock date',
  `create_time` datetime DEFAULT NULL COMMENT 'Create time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  PRIMARY KEY (`part_id`),
  UNIQUE KEY `uk_part_number` (`part_number`),
  KEY `idx_category` (`category`),
  KEY `idx_manufacturer` (`manufacturer`),
  KEY `idx_supplier_id` (`supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Auto Parts Inventory Table';

-- Create inventory movement table
CREATE TABLE `crm_inventory_movement` (
  `movement_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Movement ID',
  `part_id` bigint(20) NOT NULL COMMENT 'Part ID',
  `part_number` varchar(50) DEFAULT NULL COMMENT 'Part number',
  `part_name` varchar(100) DEFAULT NULL COMMENT 'Part name',
  `movement_type` varchar(20) NOT NULL COMMENT 'Movement type',
  `quantity_changed` int(11) NOT NULL COMMENT 'Quantity changed',
  `previous_quantity` int(11) DEFAULT NULL COMMENT 'Previous quantity',
  `current_quantity` int(11) DEFAULT NULL COMMENT 'Current quantity',
  `movement_date` datetime DEFAULT NULL COMMENT 'Date of movement',
  `reference` varchar(100) DEFAULT NULL COMMENT 'Reference document',
  `notes` text COMMENT 'Notes',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Created by',
  `create_time` datetime DEFAULT NULL COMMENT 'Create time',
  PRIMARY KEY (`movement_id`),
  KEY `idx_part_id` (`part_id`),
  KEY `idx_movement_date` (`movement_date`),
  KEY `idx_movement_type` (`movement_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Inventory Movement History Table';

-- Add menu items for Auto Parts Management
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1401, 0, 'Auto Parts Management', NULL, NULL, 0, 'icon-car', 7);

-- Auto Parts menu
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1402, 1401, 'Parts Inventory', 'modules/autoparts/part', NULL, 1, 'icon-part', 1);

-- Inventory Movements menu
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1403, 1401, 'Inventory Movements', 'modules/autoparts/movement', NULL, 1, 'icon-movement', 2);

-- Low Stock Alert menu
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1404, 1401, 'Low Stock Alert', 'modules/autoparts/lowstock', NULL, 1, 'icon-alert', 3);

-- Add permissions for Auto Parts
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1405, 1402, 'View', NULL, 'autoparts:part:list,autoparts:part:info', 2, NULL, 0);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1406, 1402, 'Add', NULL, 'autoparts:part:save', 2, NULL, 1);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1407, 1402, 'Edit', NULL, 'autoparts:part:update', 2, NULL, 2);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1408, 1402, 'Delete', NULL, 'autoparts:part:delete', 2, NULL, 3);

-- Add permissions for Inventory Movements
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1409, 1403, 'View', NULL, 'autoparts:movement:list,autoparts:movement:info', 2, NULL, 0);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1410, 1403, 'Add', NULL, 'autoparts:movement:save', 2, NULL, 1);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1411, 1403, 'Edit', NULL, 'autoparts:movement:update', 2, NULL, 2);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1412, 1403, 'Delete', NULL, 'autoparts:movement:delete', 2, NULL, 3);

-- Add permissions for Low Stock Alert
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1413, 1404, 'View', NULL, 'autoparts:lowstock:list', 2, NULL, 0);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1414, 1404, 'Export', NULL, 'autoparts:lowstock:export', 2, NULL, 1); 