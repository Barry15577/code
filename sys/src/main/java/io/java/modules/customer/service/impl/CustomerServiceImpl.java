package io.java.modules.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.PageUtils;
import io.java.common.utils.Query;
import io.java.modules.customer.dao.CustomerDao;
import io.java.modules.customer.entity.CustomerEntity;
import io.java.modules.customer.service.CustomerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Customer Service Implementation
 * Implements business operations for customer management
 *
 * @author Jiasi Lu
 */
@Service("customerService")
public class CustomerServiceImpl extends ServiceImpl<CustomerDao, CustomerEntity> implements CustomerService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String customerName = (String) params.get("customerName");
        String email = (String) params.get("email");
        String status = (String) params.get("status");

        // Build query conditions
        QueryWrapper<CustomerEntity> queryWrapper = new QueryWrapper<>();
        
        // Add filters if provided
        if (StringUtils.isNotBlank(customerName)) {
            queryWrapper.like("customer_name", customerName);
        }
        
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.like("email", email);
        }
        
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("status", status);
        }
        
        // Sort by create time descending
        queryWrapper.orderByDesc("create_time");
        
        // Execute paginated query
        IPage<CustomerEntity> page = this.page(
            new Query<CustomerEntity>().getPage(params),
            queryWrapper
        );
        
        return new PageUtils(page);
    }

    @Override
    public CustomerEntity getCustomerById(Long customerId) {
        return this.getById(customerId);
    }

    @Override
    public void saveCustomer(CustomerEntity customer) {
        // Set default values
        if (customer.getStatus() == null) {
            customer.setStatus(1); // Default to active
        }
        
        customer.setCreateTime(new Date());
        customer.setUpdateTime(new Date());
        
        this.save(customer);
    }

    @Override
    public void updateCustomer(CustomerEntity customer) {
        customer.setUpdateTime(new Date());
        this.updateById(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        // Consider implementing soft delete if needed
        this.removeById(customerId);
    }
} 