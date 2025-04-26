package io.java.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysUserNotificationReadEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * System User Notification Read DAO
 * Data access object for tracking which notifications have been read by users
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysUserNotificationReadDao extends BaseMapper<SysUserNotificationReadEntity> {
} 