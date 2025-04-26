package io.java.modules.customer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.customer.entity.CustomerEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Customer DAO Interface
 * Data access object for customer information
 *
 * @author Jiasi Lu
 */
@Mapper
public interface CustomerDao extends BaseMapper<CustomerEntity> {
    
} 