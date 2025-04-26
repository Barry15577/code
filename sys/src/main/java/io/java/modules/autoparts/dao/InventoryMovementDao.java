package io.java.modules.autoparts.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.autoparts.entity.InventoryMovementEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jiasi Lu
 */
@Mapper
public interface InventoryMovementDao extends BaseMapper<InventoryMovementEntity> {
    
} 