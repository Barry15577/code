package io.java.modules.supplier.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.supplier.entity.SupplierEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Supplier DAO
 * Data access object for supplier information
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SupplierDao extends BaseMapper<SupplierEntity> {
    
} 