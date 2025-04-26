package io.java.modules.autoparts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.autoparts.entity.AutoPartEntity;

import java.util.List;
import java.util.Map;

/**
 * Auto Part Service
 * Defines business operations for managing auto parts
 *
 * @author Jiasi Lu
 */
public interface AutoPartService extends IService<AutoPartEntity> {
    
    /**
     * Get a paginated list of auto parts based on query parameters
     *
     * @param params Query parameters including page, limit, and filters
     * @return Paginated auto part list
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * Get auto part details by ID
     *
     * @param partId Part ID
     * @return Auto part entity
     */
    AutoPartEntity getPartById(Long partId);
    
    /**
     * Get auto parts by ID prefix
     * Used to handle JavaScript precision issues with large IDs
     *
     * @param idPrefix Prefix of the part ID (first digits)
     * @return List of auto parts matching the prefix
     */
    List<AutoPartEntity> getPartsByIdPrefix(String idPrefix);
    
    /**
     * Save a new auto part
     *
     * @param part Auto part entity to save
     */
    void savePart(AutoPartEntity part);
    
    /**
     * Update an existing auto part
     *
     * @param part Auto part entity with updated information
     */
    void updatePart(AutoPartEntity part);
    
    /**
     * Delete an auto part by ID
     *
     * @param partId Part ID to delete
     */
    void deletePart(Long partId);
    
    /**
     * Check if a part number already exists
     *
     * @param partNumber Part number to check
     * @param excludeId Part ID to exclude from the check (for updates)
     * @return true if the part number exists, false otherwise
     */
    boolean checkPartNumberExists(String partNumber, Long excludeId);
    
    /**
     * Update stock quantity
     *
     * @param partId Part ID
     * @param quantityChange Quantity to add (positive) or subtract (negative)
     * @param movementType Type of movement (Stock In, Stock Out, Adjustment, Return)
     * @param reference Reference document
     * @param notes Additional notes
     * @param createdBy User who performed this movement
     * @return Updated auto part entity
     */
    AutoPartEntity updateStock(Long partId, Integer quantityChange, String movementType, 
                             String reference, String notes, String createdBy);
    
    /**
     * Get list of parts that are below their low stock threshold
     *
     * @return List of parts with low stock
     */
    List<AutoPartEntity> getLowStockParts();
    
    /**
     * Get list of distinct categories
     *
     * @return List of categories
     */
    List<String> getCategories();
    
    /**
     * Get list of distinct subcategories for a given category
     *
     * @param category Category name
     * @return List of subcategories
     */
    List<String> getSubcategories(String category);
    
    /**
     * Get list of distinct manufacturers
     *
     * @return List of manufacturers
     */
    List<String> getManufacturers();
} 