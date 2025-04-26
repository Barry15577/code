package io.java.modules.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.customer.entity.CustomerEntity;

import java.util.Map;

/**
 * Customer Service Interface
 * Defines business operations for customer management
 *
 * @author Jiasi Lu
 */
public interface CustomerService extends IService<CustomerEntity> {

    /**
     * Get paginated customer list with optional filters
     *
     * @param params Query parameters including page info and filter conditions
     * @return Paginated result containing customers
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * Get customer details by id
     *
     * @param customerId Customer ID
     * @return Customer entity with detailed information
     */
    CustomerEntity getCustomerById(Long customerId);
    
    /**
     * Save new customer
     *
     * @param customer Customer information
     */
    void saveCustomer(CustomerEntity customer);
    
    /**
     * Update existing customer
     *
     * @param customer Customer information to update
     */
    void updateCustomer(CustomerEntity customer);
    
    /**
     * Delete customer by id
     *
     * @param customerId Customer ID
     */
    void deleteCustomer(Long customerId);
} 