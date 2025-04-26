package io.java.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysNotificationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * System Notification Mapper
 * Data access object for system notifications
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysNotificationMapper extends BaseMapper<SysNotificationEntity> {
} 