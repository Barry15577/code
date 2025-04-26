-- Create supplier table
CREATE TABLE `crm_supplier` (
  `supplier_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Supplier ID',
  `supplier_code` varchar(50) NOT NULL COMMENT 'Supplier code',
  `supplier_name` varchar(100) NOT NULL COMMENT 'Supplier name',
  `supplier_type` varchar(50) DEFAULT NULL COMMENT 'Supplier type',
  `contact_person` varchar(100) DEFAULT NULL COMMENT 'Contact person',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT 'Contact phone',
  `contact_email` varchar(100) DEFAULT NULL COMMENT 'Contact email',
  `address` varchar(255) DEFAULT NULL COMMENT 'Address',
  `website` varchar(255) DEFAULT NULL COMMENT 'Website',
  `registration_number` varchar(50) DEFAULT NULL COMMENT 'Registration number',
  `established_year` int(11) DEFAULT NULL COMMENT 'Year established',
  `annual_revenue` decimal(20,2) DEFAULT NULL COMMENT 'Annual revenue',
  `employee_count` int(11) DEFAULT NULL COMMENT 'Employee count',
  `industry` varchar(100) DEFAULT NULL COMMENT 'Industry',
  `main_products` varchar(255) DEFAULT NULL COMMENT 'Main products/services',
  `payment_terms` varchar(255) DEFAULT NULL COMMENT 'Payment terms',
  `description` text COMMENT 'Description',
  `rating` int(11) DEFAULT NULL COMMENT 'Rating (1-5)',
  `status` tinyint(4) DEFAULT '1' COMMENT 'Status: 0-Inactive, 1-Active',
  `create_time` datetime DEFAULT NULL COMMENT 'Create time',
  `update_time` datetime DEFAULT NULL COMMENT 'Update time',
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `uk_supplier_code` (`supplier_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Supplier Information Table';

-- Add menu items
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1301, 0, 'Supplier Management', NULL, NULL, 0, 'icon-supplier', 6);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1302, 1301, 'Supplier List', 'modules/supplier/supplier', NULL, 1, 'icon-list', 1);

-- Add supplier menu permissions
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1303, 1302, 'View', NULL, 'supplier:supplier:list,supplier:supplier:info', 2, NULL, 0);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1304, 1302, 'Add', NULL, 'supplier:supplier:save', 2, NULL, 1);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1305, 1302, 'Edit', NULL, 'supplier:supplier:update', 2, NULL, 2);

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) 
VALUES (1306, 1302, 'Delete', NULL, 'supplier:supplier:delete', 2, NULL, 3); 