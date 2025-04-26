package io.java.modules.insurance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.insurance.entity.InsuranceProductEntity;

import java.util.Map;

/**
 * Insurance Product Service Interface
 * Defines business operations for insurance product management
 *
 * @author Jiasi Lu
 */
public interface InsuranceProductService extends IService<InsuranceProductEntity> {

    /**
     * Get paginated insurance product list with optional filters
     *
     * @param params Query parameters including page info and filter conditions
     * @return Paginated result containing insurance products
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * Get insurance product details by id
     *
     * @param productId Product ID
     * @return Insurance product entity with detailed information
     */
    InsuranceProductEntity getProductById(Long productId);
    
    /**
     * Save new insurance product
     *
     * @param product Insurance product information
     */
    void saveProduct(InsuranceProductEntity product);
    
    /**
     * Update existing insurance product
     *
     * @param product Insurance product information to update
     */
    void updateProduct(InsuranceProductEntity product);
    
    /**
     * Delete insurance product by id
     *
     * @param productId Product ID
     */
    void deleteProduct(Long productId);
    
    /**
     * Check if product code exists
     *
     * @param productCode Product code to check
     * @param excludeId Product ID to exclude from check (for updates)
     * @return true if exists, false otherwise
     */
    boolean checkProductCodeExists(String productCode, Long excludeId);
} 