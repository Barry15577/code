package io.java.modules.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.PageUtils;
import io.java.common.utils.Query;
import io.java.modules.insurance.dao.InsuranceOrderDao;
import io.java.modules.insurance.entity.InsuranceOrderEntity;
import io.java.modules.insurance.service.InsuranceOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Insurance Order Service Implementation
 *
 * @author Jiasi Lu
 */
@Service("insuranceOrderService")
public class InsuranceOrderServiceImpl extends ServiceImpl<InsuranceOrderDao, InsuranceOrderEntity> implements InsuranceOrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String customerName = (String) params.get("customerName");
        String productName = (String) params.get("productName");
        
        // Safely convert status to Integer
        Integer status = null;
        Object statusObj = params.get("status");
        if (statusObj != null) {
            if (statusObj instanceof Integer) {
                status = (Integer) statusObj;
            } else if (statusObj instanceof String) {
                String statusStr = (String) statusObj;
                if (StringUtils.isNotBlank(statusStr)) {
                    try {
                        status = Integer.parseInt(statusStr);
                    } catch (NumberFormatException e) {
                        // If parsing fails, leave status as null
                    }
                }
            }
        }
        
        // Build query conditions
        QueryWrapper<InsuranceOrderEntity> queryWrapper = new QueryWrapper<InsuranceOrderEntity>()
            .like(StringUtils.isNotBlank(customerName), "customer_name", customerName)
            .like(StringUtils.isNotBlank(productName), "product_name", productName)
            .eq(status != null, "status", status)
            .orderByDesc("create_time");
        
        // Execute query with pagination
        IPage<InsuranceOrderEntity> page = this.page(
                new Query<InsuranceOrderEntity>().getPage(params),
                queryWrapper
        );
        
        return new PageUtils(page);
    }

    @Override
    public InsuranceOrderEntity getOrderById(Long orderId) {
        return this.getById(orderId);
    }

    @Override
    public void saveOrder(InsuranceOrderEntity order) {
        // Set default values
        order.setStatus(0); // Pending status
        order.setCreateTime(new Date());
        
        this.save(order);
    }

    @Override
    public void updateOrder(InsuranceOrderEntity order) {
        order.setUpdateTime(new Date());
        this.updateById(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        InsuranceOrderEntity order = this.getById(orderId);
        if (order != null) {
            order.setStatus(2); // Cancelled status
            order.setUpdateTime(new Date());
            this.updateById(order);
        }
    }

    @Override
    public void confirmOrder(Long orderId, String processedBy) {
        InsuranceOrderEntity order = this.getById(orderId);
        if (order != null) {
            order.setStatus(1); // Confirmed status
            order.setProcessedBy(processedBy);
            order.setProcessedTime(new Date());
            order.setUpdateTime(new Date());
            this.updateById(order);
        }
    }

    @Override
    public void completeOrder(Long orderId, String processedBy) {
        InsuranceOrderEntity order = this.getById(orderId);
        if (order != null) {
            order.setStatus(3); // Completed status
            order.setProcessedBy(processedBy);
            order.setProcessedTime(new Date());
            order.setUpdateTime(new Date());
            this.updateById(order);
        }
    }
} 