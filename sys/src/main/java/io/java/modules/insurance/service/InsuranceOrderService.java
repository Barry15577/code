package io.java.modules.insurance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.insurance.entity.InsuranceOrderEntity;

import java.util.Map;

/**
 * Insurance Order Service
 * Service interface for insurance product order operations
 *
 * @author Jiasi Lu
 */
public interface InsuranceOrderService extends IService<InsuranceOrderEntity> {
    /**
     * Query page with various conditions
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * Get order details by ID
     */
    InsuranceOrderEntity getOrderById(Long orderId);
    
    /**
     * Save new order
     */
    void saveOrder(InsuranceOrderEntity order);
    
    /**
     * Update order
     */
    void updateOrder(InsuranceOrderEntity order);
    
    /**
     * Cancel order by ID
     */
    void cancelOrder(Long orderId);
    
    /**
     * Confirm order by ID
     */
    void confirmOrder(Long orderId, String processedBy);
    
    /**
     * Complete order by ID
     */
    void completeOrder(Long orderId, String processedBy);
} 