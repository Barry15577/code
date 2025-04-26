package io.java.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.oss.entity.SysOssEntity;

import java.util.Map;

/**
 * File upload
 *
 * @author Jiasi Lu
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
