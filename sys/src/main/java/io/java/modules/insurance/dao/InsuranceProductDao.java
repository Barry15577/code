package io.java.modules.insurance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.insurance.entity.InsuranceProductEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Insurance Product DAO Interface
 * Data access object for insurance product information
 *
 * @author Jiasi Lu
 */
@Mapper
public interface InsuranceProductDao extends BaseMapper<InsuranceProductEntity> {
    
} 