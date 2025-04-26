package io.java.modules.insurance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.insurance.entity.InsuranceOrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Insurance Order DAO
 * Data access object for handling insurance product order information
 *
 * @author Jiasi Lu
 */
@Mapper
public interface InsuranceOrderDao extends BaseMapper<InsuranceOrderEntity> {
} 