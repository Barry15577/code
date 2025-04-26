

package io.java.modules.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.java.modules.oss.entity.SysOssEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * File upload
 *
 * @author Jiasi Lu
 */
@Mapper
public interface SysOssDao extends BaseMapper<SysOssEntity> {
	
}
