package io.java.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.java.common.utils.R;
import io.java.modules.sys.entity.SysUserTokenEntity;

/**
 * User Token
 *
 * @author Jiasi Lu
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * Generate token
	 * @param userId User ID
	 */
	R createToken(long userId);

	/**
	 * Exit, Edittoken value
	 * @param userId User ID
	 */
	void logout(long userId);

}
