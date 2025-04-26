package io.java.modules.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.customer.entity.ContactRecordEntity;

import java.util.Map;

/**
 * Contact Record Service Interface
 * Defines business operations for customer contact record management
 *
 * @author Jiasi Lu
 */
public interface ContactRecordService extends IService<ContactRecordEntity> {

    /**
     * Get paginated contact records with optional filters
     *
     * @param params Query parameters including page info and filter conditions
     * @return Paginated result containing contact records
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * Get contact record details by id
     *
     * @param contactId Contact record ID
     * @return Contact record entity with detailed information
     */
    ContactRecordEntity getContactRecordById(Long contactId);
    
    /**
     * Save new contact record
     *
     * @param contactRecord Contact record information
     */
    void saveContactRecord(ContactRecordEntity contactRecord);
    
    /**
     * Update existing contact record
     *
     * @param contactRecord Contact record information to update
     */
    void updateContactRecord(ContactRecordEntity contactRecord);
    
    /**
     * Delete contact record by id
     *
     * @param contactId Contact record ID
     */
    void deleteContactRecord(Long contactId);
    
    /**
     * Get contact records for a specific customer
     *
     * @param params Query parameters including customerId and page info
     * @return Paginated result containing contact records for the customer
     */
    PageUtils queryCustomerContacts(Map<String, Object> params);
} 