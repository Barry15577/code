package io.java.modules.supplier.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.PageUtils;
import io.java.common.utils.Query;
import io.java.modules.supplier.dao.SupplierDao;
import io.java.modules.supplier.entity.SupplierEntity;
import io.java.modules.supplier.service.SupplierService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Supplier Service Implementation
 * Implements business operations for managing suppliers
 *
 * @author Jiasi Lu
 */
@Service("supplierService")
public class SupplierServiceImpl extends ServiceImpl<SupplierDao, SupplierEntity> implements SupplierService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String supplierName = (String) params.get("supplierName");
        String supplierType = (String) params.get("supplierType");
        String contactPerson = (String) params.get("contactPerson");
        
        // Handle status parameter (can be Integer or String)
        Object statusObj = params.get("status");
        Integer status = null;
        
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
        QueryWrapper<SupplierEntity> queryWrapper = new QueryWrapper<>();
        
        // Add filters if provided
        if (StringUtils.isNotBlank(supplierName)) {
            queryWrapper.like("supplier_name", supplierName);
        }
        
        if (StringUtils.isNotBlank(supplierType)) {
            queryWrapper.eq("supplier_type", supplierType);
        }
        
        if (StringUtils.isNotBlank(contactPerson)) {
            queryWrapper.like("contact_person", contactPerson);
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // Sort by create time descending
        queryWrapper.orderByDesc("create_time");
        
        // Execute paginated query
        IPage<SupplierEntity> page = this.page(
                new Query<SupplierEntity>().getPage(params),
                queryWrapper
        );
        
        return new PageUtils(page);
    }
    
    @Override
    public List<SupplierEntity> listAllActiveSuppliers() {
        QueryWrapper<SupplierEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 1 = Active
        queryWrapper.select("supplier_id", "supplier_code", "supplier_name"); // Only select necessary fields
        queryWrapper.orderByAsc("supplier_name"); // Sort by name for dropdown
        
        return this.list(queryWrapper);
    }
    
    @Override
    public SupplierEntity getSupplierById(Long supplierId) {
        return this.getById(supplierId);
    }
    
    @Override
    public void saveSupplier(SupplierEntity supplier) {
        // Set default values
        if (supplier.getStatus() == null) {
            supplier.setStatus(1); // Default to active
        }
        
        supplier.setCreateTime(new Date());
        supplier.setUpdateTime(new Date());
        
        this.save(supplier);
    }
    
    @Override
    public void updateSupplier(SupplierEntity supplier) {
        supplier.setUpdateTime(new Date());
        this.updateById(supplier);
    }
    
    @Override
    public void deleteSupplier(Long supplierId) {
        this.removeById(supplierId);
    }
    
    @Override
    public boolean checkSupplierCodeExists(String supplierCode, Long excludeId) {
        QueryWrapper<SupplierEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("supplier_code", supplierCode);
        
        // If excludeId is provided, exclude that supplier from the check
        if (excludeId != null) {
            queryWrapper.ne("supplier_id", excludeId);
        }
        
        return this.count(queryWrapper) > 0;
    }
} 