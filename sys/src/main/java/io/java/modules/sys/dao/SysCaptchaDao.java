package io.java.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysCaptchaEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Verification code
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
