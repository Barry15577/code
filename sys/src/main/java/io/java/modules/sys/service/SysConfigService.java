package io.java.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.PageUtils;
import io.java.modules.sys.entity.SysConfigEntity;

import java.util.Map;

/**
 * System configuration information
 *
 * @author Jiasi Lu
 */
public interface SysConfigService extends IService<SysConfigEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * Save configuration information
	 */
	public void saveConfig(SysConfigEntity config);

	/**
	 * Update configuration information
	 */
	public void update(SysConfigEntity config);

	/**
	 * Update value according to key
	 */
	public void updateValueByKey(String key, String value);

	/**
	 * Delete configuration information
	 */
	public void deleteBatch(Long[] ids);

	/**
	 * Get the value of the configuration according to key
	 *
	 * @param key key
	 */
	public String getValue(String key);

	/**
	 * Get the value of the object according to key
	 * @param key key
	 * @param clazz Object object
	 */
	public <T> T getConfigObject(String key, Class<T> clazz);

}