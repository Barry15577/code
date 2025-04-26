package io.java.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;
/**
 * System user Token
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {

    SysUserTokenEntity queryByToken(String token);
	
}
