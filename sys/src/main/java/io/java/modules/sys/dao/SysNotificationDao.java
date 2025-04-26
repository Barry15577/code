package io.java.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysNotificationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * System Notification DAO
 * Data access object for system notifications
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysNotificationDao extends BaseMapper<SysNotificationEntity> {
} 