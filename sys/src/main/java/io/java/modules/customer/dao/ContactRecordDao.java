package io.java.modules.customer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.customer.entity.ContactRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Contact Record DAO Interface
 * Data access object for customer contact records
 *
 * @author Jiasi Lu
 */
@Mapper
public interface ContactRecordDao extends BaseMapper<ContactRecordEntity> {
    
} 