package io.java.modules.autoparts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.PageUtils;
import io.java.common.utils.Query;
import io.java.modules.autoparts.dao.InventoryMovementDao;
import io.java.modules.autoparts.entity.InventoryMovementEntity;
import io.java.modules.autoparts.service.InventoryMovementService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Inventory Movement Service Implementation
 * Implements business operations for managing inventory movements
 *
 * @author Jiasi Lu
 */
@Service("inventoryMovementService")
public class InventoryMovementServiceImpl extends ServiceImpl<InventoryMovementDao, InventoryMovementEntity> implements InventoryMovementService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String partNumber = (String) params.get("partNumber");
        String partName = (String) params.get("partName");
        String movementType = (String) params.get("movementType");
        String reference = (String) params.get("reference");
        String createdBy = (String) params.get("createdBy");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        
        // Build query conditions
        QueryWrapper<InventoryMovementEntity> queryWrapper = new QueryWrapper<>();
        
        // Add filters if provided
        if (StringUtils.isNotBlank(partNumber)) {
            queryWrapper.like("part_number", partNumber);
        }
        
        if (StringUtils.isNotBlank(partName)) {
            queryWrapper.like("part_name", partName);
        }
        
        if (StringUtils.isNotBlank(movementType)) {
            queryWrapper.eq("movement_type", movementType);
        }
        
        if (StringUtils.isNotBlank(reference)) {
            queryWrapper.like("reference", reference);
        }
        
        if (StringUtils.isNotBlank(createdBy)) {
            queryWrapper.like("created_by", createdBy);
        }
        
        // Handle date range
        if (StringUtils.isNotBlank(startDate)) {
            queryWrapper.ge("movement_date", startDate + " 00:00:00");
        }
        
        if (StringUtils.isNotBlank(endDate)) {
            queryWrapper.le("movement_date", endDate + " 23:59:59");
        }
        
        // Sort by movement date descending
        queryWrapper.orderByDesc("movement_date");
        
        // Execute paginated query
        IPage<InventoryMovementEntity> page = this.page(
                new Query<InventoryMovementEntity>().getPage(params),
                queryWrapper
        );
        
        return new PageUtils(page);
    }
    
    @Override
    public InventoryMovementEntity getMovementById(Long movementId) {
        return this.getById(movementId);
    }
    
    @Override
    public void saveMovement(InventoryMovementEntity movement) {
        if (movement.getMovementDate() == null) {
            movement.setMovementDate(new Date());
        }
        
        if (movement.getCreateTime() == null) {
            movement.setCreateTime(new Date());
        }
        
        this.save(movement);
    }
    
    @Override
    public PageUtils getMovementHistoryByPartId(Long partId, Map<String, Object> params) {
        QueryWrapper<InventoryMovementEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("part_id", partId)
                .orderByDesc("movement_date");
        
        // Execute paginated query
        IPage<InventoryMovementEntity> page = this.page(
                new Query<InventoryMovementEntity>().getPage(params),
                queryWrapper
        );
        
        return new PageUtils(page);
    }
    
    @Override
    public List<InventoryMovementEntity> getRecentMovements(int limit) {
        QueryWrapper<InventoryMovementEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("movement_date")
                .last("LIMIT " + limit);
        
        return this.list(queryWrapper);
    }
    
    @Override
    public Map<String, Object> getMovementTypeSummary(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        
        QueryWrapper<InventoryMovementEntity> queryWrapper = new QueryWrapper<>();
        
        // Handle date range
        if (StringUtils.isNotBlank(startDate)) {
            queryWrapper.ge("movement_date", startDate + " 00:00:00");
        }
        
        if (StringUtils.isNotBlank(endDate)) {
            queryWrapper.le("movement_date", endDate + " 23:59:59");
        }
        
        // Get all relevant movements
        List<InventoryMovementEntity> movements = this.list(queryWrapper);
        
        // Prepare summary data
        Map<String, Integer> typeCount = new HashMap<>();
        Map<String, Integer> typeQuantity = new HashMap<>();
        
        for (InventoryMovementEntity movement : movements) {
            String type = movement.getMovementType();
            Integer count = typeCount.getOrDefault(type, 0);
            Integer quantity = typeQuantity.getOrDefault(type, 0);
            
            typeCount.put(type, count + 1);
            typeQuantity.put(type, quantity + movement.getQuantityChanged());
        }
        
        result.put("typeCount", typeCount);
        result.put("typeQuantity", typeQuantity);
        result.put("totalCount", movements.size());
        
        // Format date range for response
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotBlank(startDate)) {
            result.put("startDate", startDate);
        } else {
            result.put("startDate", "All time");
        }
        
        if (StringUtils.isNotBlank(endDate)) {
            result.put("endDate", endDate);
        } else {
            result.put("endDate", sdf.format(new Date()));
        }
        
        return result;
    }
} 