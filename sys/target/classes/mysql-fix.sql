-- Script to fix MySQL reserved keyword errors in the crm_auto_part table
-- The 'condition' column is a MySQL reserved keyword and needs backquotes

-- First check if the column exists
SELECT COUNT(*) FROM information_schema.columns 
WHERE table_schema = DATABASE() 
  AND table_name = 'crm_auto_part' 
  AND column_name = 'condition';
  
-- Add backticks to the 'condition' column
ALTER TABLE `crm_auto_part` CHANGE COLUMN `condition` `condition` VARCHAR(20) DEFAULT 'New' COMMENT 'Part condition';

-- Verify the information schema now shows the correct column definition
SELECT column_name, column_type, column_default, is_nullable, column_comment
FROM information_schema.columns
WHERE table_schema = DATABASE()
  AND table_name = 'crm_auto_part'
  AND column_name = 'condition';

-- Test insert with backquoted 'condition' column
-- Replace with actual values needed
INSERT INTO `crm_auto_part` (
  `part_number`, 
  `part_name`, 
  `category`, 
  `subcategory`, 
  `manufacturer`, 
  `stock_quantity`, 
  `low_stock_threshold`, 
  `purchase_price`, 
  `selling_price`, 
  `supplier_name`, 
  `condition`,  -- Note the reserved keyword without quotes
  `status`, 
  `create_time`, 
  `update_time`
) VALUES (
  'TEST-001', 
  'Test Part', 
  'Engine Parts', 
  'Pistons', 
  'Bosch', 
  10, 
  5, 
  100.00, 
  150.00, 
  'Test Supplier', 
  'New',  -- Value for condition
  1, 
  NOW(), 
  NOW()
); 