package io.java.modules.autoparts.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.autoparts.entity.AutoPartEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jiasi Lu
 */
@Mapper
public interface AutoPartDao extends BaseMapper<AutoPartEntity> {
    
} 