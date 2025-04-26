package io.java.modules.supplier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.supplier.entity.SupplierEntity;

import java.util.List;
import java.util.Map;

/**
 * Supplier Service
 * Defines business operations for managing suppliers
 *
 * @author Jiasi Lu
 */
public interface SupplierService extends IService<SupplierEntity> {
    
    /**
     * Get a paginated list of suppliers based on query parameters
     *
     * @param params Query parameters including page, limit, and filters
     * @return Paginated supplier list
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * Get all active suppliers for dropdown selection
     *
     * @return List of active suppliers
     */
    List<SupplierEntity> listAllActiveSuppliers();
    
    /**
     * Get supplier details by ID
     *
     * @param supplierId Supplier ID
     * @return Supplier entity
     */
    SupplierEntity getSupplierById(Long supplierId);
    
    /**
     * Save a new supplier
     *
     * @param supplier Supplier entity to save
     */
    void saveSupplier(SupplierEntity supplier);
    
    /**
     * Update an existing supplier
     *
     * @param supplier Supplier entity with updated information
     */
    void updateSupplier(SupplierEntity supplier);
    
    /**
     * Delete a supplier by ID
     *
     * @param supplierId Supplier ID to delete
     */
    void deleteSupplier(Long supplierId);
    
    /**
     * Check if a supplier code already exists
     *
     * @param supplierCode Supplier code to check
     * @param excludeId Supplier ID to exclude from the check (for updates)
     * @return true if the supplier code exists, false otherwise
     */
    boolean checkSupplierCodeExists(String supplierCode, Long excludeId);
} 