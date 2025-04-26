package io.java.modules.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.PageUtils;
import io.java.common.utils.Query;
import io.java.modules.customer.dao.ContactRecordDao;
import io.java.modules.customer.entity.ContactRecordEntity;
import io.java.modules.customer.service.ContactRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Contact Record Service Implementation
 * Implements business operations for customer contact record management
 *
 * @author Jiasi Lu
 */
@Service("contactRecordService")
public class ContactRecordServiceImpl extends ServiceImpl<ContactRecordDao, ContactRecordEntity> implements ContactRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String contactType = (String) params.get("contactType");
        String contactPerson = (String) params.get("contactPerson");

        // Build query conditions
        QueryWrapper<ContactRecordEntity> queryWrapper = new QueryWrapper<>();
        
        // Add filters if provided
        if (StringUtils.isNotBlank(contactType)) {
            queryWrapper.eq("contact_type", contactType);
        }
        
        if (StringUtils.isNotBlank(contactPerson)) {
            queryWrapper.like("contact_person", contactPerson);
        }
        
        // Sort by contact date descending
        queryWrapper.orderByDesc("contact_date");
        
        // Execute paginated query
        IPage<ContactRecordEntity> page = this.page(
            new Query<ContactRecordEntity>().getPage(params),
            queryWrapper
        );
        
        return new PageUtils(page);
    }

    @Override
    public ContactRecordEntity getContactRecordById(Long contactId) {
        return this.getById(contactId);
    }

    @Override
    public void saveContactRecord(ContactRecordEntity contactRecord) {
        contactRecord.setCreateTime(new Date());
        contactRecord.setUpdateTime(new Date());
        
        this.save(contactRecord);
    }

    @Override
    public void updateContactRecord(ContactRecordEntity contactRecord) {
        contactRecord.setUpdateTime(new Date());
        this.updateById(contactRecord);
    }

    @Override
    public void deleteContactRecord(Long contactId) {
        this.removeById(contactId);
    }

    @Override
    public PageUtils queryCustomerContacts(Map<String, Object> params) {
        Long customerId = null;
        try {
            customerId = Long.parseLong((String) params.get("customerId"));
        } catch (Exception e) {
            // Handle invalid customerId parameter
        }
        
        // Build query conditions
        QueryWrapper<ContactRecordEntity> queryWrapper = new QueryWrapper<>();
        
        // Filter by customer ID
        if (customerId != null) {
            queryWrapper.eq("customer_id", customerId);
        }
        
        // Sort by contact date descending
        queryWrapper.orderByDesc("contact_date");
        
        // Execute paginated query
        IPage<ContactRecordEntity> page = this.page(
            new Query<ContactRecordEntity>().getPage(params),
            queryWrapper
        );
        
        return new PageUtils(page);
    }
} 