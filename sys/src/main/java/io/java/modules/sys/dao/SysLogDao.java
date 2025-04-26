package io.java.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * System log
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {
	
}
