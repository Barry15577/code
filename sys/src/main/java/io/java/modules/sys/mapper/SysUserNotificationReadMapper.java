package io.java.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysUserNotificationReadEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * System User Notification Read Mapper
 * Data access object for tracking which notifications have been read by users
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysUserNotificationReadMapper extends BaseMapper<SysUserNotificationReadEntity> {
} 