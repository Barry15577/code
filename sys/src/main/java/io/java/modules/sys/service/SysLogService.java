package io.java.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.sys.entity.SysLogEntity;

import java.util.Map;
/**
 * System log
 *
 * @author Jiasi Lu
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
