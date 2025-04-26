package io.java.modules.autoparts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.autoparts.entity.InventoryMovementEntity;

import java.util.List;
import java.util.Map;

/**
 * Inventory Movement Service
 * Defines business operations for managing inventory movements
 *
 * @author Jiasi Lu
 */
public interface InventoryMovementService extends IService<InventoryMovementEntity> {
    
    /**
     * Get a paginated list of inventory movements based on query parameters
     *
     * @param params Query parameters including page, limit, and filters
     * @return Paginated inventory movement list
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * Get inventory movement details by ID
     *
     * @param movementId Movement ID
     * @return Inventory movement entity
     */
    InventoryMovementEntity getMovementById(Long movementId);
    
    /**
     * Save a new inventory movement
     *
     * @param movement Inventory movement entity to save
     */
    void saveMovement(InventoryMovementEntity movement);
    
    /**
     * Get inventory movement history for a specific part
     *
     * @param partId Part ID
     * @param params Query parameters including page, limit, and filters
     * @return Paginated inventory movement list for the specified part
     */
    PageUtils getMovementHistoryByPartId(Long partId, Map<String, Object> params);
    
    /**
     * Get recent movements across all parts
     *
     * @param limit Maximum number of movements to return
     * @return List of recent inventory movements
     */
    List<InventoryMovementEntity> getRecentMovements(int limit);
    
    /**
     * Get summary of movement types for reporting
     *
     * @param params Parameters including date range
     * @return Map of movement types and their counts/quantities
     */
    Map<String, Object> getMovementTypeSummary(Map<String, Object> params);
} 