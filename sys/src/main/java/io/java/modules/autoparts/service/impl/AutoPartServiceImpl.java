package io.java.modules.autoparts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.PageUtils;
import io.java.common.utils.Query;
import io.java.modules.autoparts.dao.AutoPartDao;
import io.java.modules.autoparts.entity.AutoPartEntity;
import io.java.modules.autoparts.entity.InventoryMovementEntity;
import io.java.modules.autoparts.service.AutoPartService;
import io.java.modules.autoparts.service.InventoryMovementService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Auto Part Service Implementation
 * Implements business operations for managing auto parts
 *
 * @author Jiasi Lu
 */
@Service("autoPartService")
public class AutoPartServiceImpl extends ServiceImpl<AutoPartDao, AutoPartEntity> implements AutoPartService {

    @Autowired
    private InventoryMovementService inventoryMovementService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String partNumber = (String) params.get("partNumber");
        String partName = (String) params.get("partName");
        String category = (String) params.get("category");
        String subcategory = (String) params.get("subcategory");
        String manufacturer = (String) params.get("manufacturer");
        String compatibleMake = (String) params.get("compatibleMake");
        
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
        
        // Handle low stock filter
        Object lowStockObj = params.get("lowStock");
        Boolean lowStock = null;
        if (lowStockObj != null) {
            if (lowStockObj instanceof Boolean) {
                lowStock = (Boolean) lowStockObj;
            } else if (lowStockObj instanceof String) {
                lowStock = Boolean.parseBoolean((String) lowStockObj);
            }
        }
        
        // Build query conditions
        QueryWrapper<AutoPartEntity> queryWrapper = new QueryWrapper<>();
        
        // Add filters if provided
        if (StringUtils.isNotBlank(partNumber)) {
            queryWrapper.like("part_number", partNumber);
        }
        
        if (StringUtils.isNotBlank(partName)) {
            queryWrapper.like("part_name", partName);
        }
        
        if (StringUtils.isNotBlank(category)) {
            queryWrapper.eq("category", category);
        }
        
        if (StringUtils.isNotBlank(subcategory)) {
            queryWrapper.eq("subcategory", subcategory);
        }
        
        if (StringUtils.isNotBlank(manufacturer)) {
            queryWrapper.like("manufacturer", manufacturer);
        }
        
        if (StringUtils.isNotBlank(compatibleMake)) {
            queryWrapper.like("compatible_makes", compatibleMake);
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // Filter for low stock items
        if (lowStock != null && lowStock) {
            queryWrapper.apply("stock_quantity <= low_stock_threshold");
        }
        
        // Sort by part number
        queryWrapper.orderByAsc("part_number");
        
        // Execute paginated query
        IPage<AutoPartEntity> page = this.page(
                new Query<AutoPartEntity>().getPage(params),
                queryWrapper
        );
        
        return new PageUtils(page);
    }
    
    @Override
    public AutoPartEntity getPartById(Long partId) {
        return this.getById(partId);
    }
    
    @Override
    public List<AutoPartEntity> getPartsByIdPrefix(String idPrefix) {
        QueryWrapper<AutoPartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("part_id", idPrefix)
                   .orderByAsc("part_id")
                   .last("LIMIT 10"); // Limit to 10 matches to avoid performance issues
        
        return this.list(queryWrapper);
    }
    
    @Override
    public void savePart(AutoPartEntity part) {
        // Set default values
        if (part.getStatus() == null) {
            part.setStatus(1); // Default to active
        }
        
        if (part.getStockQuantity() == null) {
            part.setStockQuantity(0); // Default stock to 0
        }
        
        if (part.getLowStockThreshold() == null) {
            part.setLowStockThreshold(5); // Default low stock threshold
        }
        
        part.setCreateTime(new Date());
        part.setUpdateTime(new Date());
        
        this.save(part);
    }
    
    @Override
    public void updatePart(AutoPartEntity part) {
        part.setUpdateTime(new Date());
        this.updateById(part);
    }
    
    @Override
    public void deletePart(Long partId) {
        this.removeById(partId);
    }
    
    @Override
    public boolean checkPartNumberExists(String partNumber, Long excludeId) {
        QueryWrapper<AutoPartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("part_number", partNumber);
        
        // If excludeId is provided, exclude that part from the check
        if (excludeId != null) {
            queryWrapper.ne("part_id", excludeId);
        }
        
        return this.count(queryWrapper) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AutoPartEntity updateStock(Long partId, Integer quantityChange, String movementType, 
                                   String reference, String notes, String createdBy) {
        // Get current part
        AutoPartEntity part = this.getById(partId);
        
        // If part not found and ID might be affected by JavaScript precision issues
        if (part == null && partId != null) {
            // Convert partId to string to get prefix for search
            String partIdStr = partId.toString();
            if (partIdStr.length() >= 15) {
                String partIdPrefix = partIdStr.substring(0, 15);
                List<AutoPartEntity> possibleParts = this.getPartsByIdPrefix(partIdPrefix);
                
                if (!possibleParts.isEmpty()) {
                    part = possibleParts.get(0); // Get the first match
                }
            }
        }
        
        // If still no part found
        if (part == null) {
            throw new RuntimeException("Part not found");
        }
        
        // Calculate new quantity
        Integer previousQuantity = part.getStockQuantity();
        Integer newQuantity = previousQuantity + quantityChange;
        
        // Prevent negative stock
        if (newQuantity < 0) {
            throw new RuntimeException("Cannot reduce stock below zero");
        }
        
        // Update part stock quantity
        part.setStockQuantity(newQuantity);
        part.setUpdateTime(new Date());
        
        if (quantityChange > 0) {
            part.setRestockDate(new Date());
        }
        
        this.updateById(part);
        
        // Create inventory movement record
        InventoryMovementEntity movement = new InventoryMovementEntity();
        movement.setPartId(part.getPartId()); // Use the part ID from the found part
        movement.setPartNumber(part.getPartNumber());
        movement.setPartName(part.getPartName());
        movement.setMovementType(movementType);
        movement.setQuantityChanged(quantityChange);
        movement.setPreviousQuantity(previousQuantity);
        movement.setCurrentQuantity(newQuantity);
        movement.setMovementDate(new Date());
        movement.setReference(reference);
        movement.setNotes(notes);
        movement.setCreatedBy(createdBy);
        movement.setCreateTime(new Date());
        
        inventoryMovementService.saveMovement(movement);
        
        return part;
    }
    
    @Override
    public List<AutoPartEntity> getLowStockParts() {
        QueryWrapper<AutoPartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("stock_quantity <= low_stock_threshold")
                .orderByAsc("stock_quantity");
        
        return this.list(queryWrapper);
    }
    
    @Override
    public List<String> getCategories() {
        QueryWrapper<AutoPartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT category")
                .isNotNull("category")
                .ne("category", "");
        
        return this.list(queryWrapper).stream()
                .map(AutoPartEntity::getCategory)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getSubcategories(String category) {
        QueryWrapper<AutoPartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT subcategory")
                .eq("category", category)
                .isNotNull("subcategory")
                .ne("subcategory", "");
        
        return this.list(queryWrapper).stream()
                .map(AutoPartEntity::getSubcategory)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getManufacturers() {
        QueryWrapper<AutoPartEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT manufacturer")
                .isNotNull("manufacturer")
                .ne("manufacturer", "");
        
        return this.list(queryWrapper).stream()
                .map(AutoPartEntity::getManufacturer)
                .collect(Collectors.toList());
    }
} 