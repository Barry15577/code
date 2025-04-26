-- Fix for the MySQL reserved keyword 'condition'
-- This script alters the column name to use backticks

-- First, create a backup of the existing table structure if needed
-- CREATE TABLE crm_auto_part_backup LIKE crm_auto_part;
-- INSERT INTO crm_auto_part_backup SELECT * FROM crm_auto_part;

-- Drop and recreate the table with proper backticks for reserved keywords
DROP TABLE IF EXISTS `crm_auto_part`;

-- Recreate the table with backticks around 'condition'
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

-- Restore data from backup if needed
-- INSERT INTO crm_auto_part SELECT * FROM crm_auto_part_backup;
-- DROP TABLE crm_auto_part_backup; 