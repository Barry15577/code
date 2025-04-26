package io.java.modules.insurance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.PageUtils;
import io.java.common.utils.Query;
import io.java.modules.insurance.dao.InsuranceProductDao;
import io.java.modules.insurance.entity.InsuranceProductEntity;
import io.java.modules.insurance.service.InsuranceProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Insurance Product Service Implementation
 * Implements business operations for insurance product management
 *
 * @author Jiasi Lu
 */
@Service("insuranceProductService")
public class InsuranceProductServiceImpl extends ServiceImpl<InsuranceProductDao, InsuranceProductEntity> implements InsuranceProductService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String productName = (String) params.get("productName");
        String productType = (String) params.get("productType");
        String provider = (String) params.get("provider");
        Object statusObj = params.get("status");
        Integer status = null;

        // Convert status to Integer if it exists
        if (statusObj != null) {
            if (statusObj instanceof Integer) {
                status = (Integer) statusObj;
            } else if (statusObj instanceof String && StringUtils.isNotBlank((String) statusObj)) {
                try {
                    status = Integer.parseInt((String) statusObj);
                } catch (NumberFormatException e) {
                    // If conversion fails, leave it as null
                }
            }
        }

        // Build query conditions
        QueryWrapper<InsuranceProductEntity> queryWrapper = new QueryWrapper<>();
        
        // Add filters if provided
        if (StringUtils.isNotBlank(productName)) {
            queryWrapper.like("product_name", productName);
        }
        
        if (StringUtils.isNotBlank(productType)) {
            queryWrapper.eq("product_type", productType);
        }
        
        if (StringUtils.isNotBlank(provider)) {
            queryWrapper.like("provider", provider);
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // Sort by create time descending
        queryWrapper.orderByDesc("create_time");
        
        // Execute paginated query
        IPage<InsuranceProductEntity> page = this.page(
            new Query<InsuranceProductEntity>().getPage(params),
            queryWrapper
        );
        
        return new PageUtils(page);
    }

    @Override
    public InsuranceProductEntity getProductById(Long productId) {
        return this.getById(productId);
    }

    @Override
    public void saveProduct(InsuranceProductEntity product) {
        // Set default values
        if (product.getStatus() == null) {
            product.setStatus(1); // Default to active
        }
        
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        
        this.save(product);
    }

    @Override
    public void updateProduct(InsuranceProductEntity product) {
        product.setUpdateTime(new Date());
        this.updateById(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        this.removeById(productId);
    }

    @Override
    public boolean checkProductCodeExists(String productCode, Long excludeId) {
        QueryWrapper<InsuranceProductEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_code", productCode);
        
        // If excludeId is provided, exclude that product from the check
        if (excludeId != null) {
            queryWrapper.ne("product_id", excludeId);
        }
        
        return this.count(queryWrapper) > 0;
    }
} 