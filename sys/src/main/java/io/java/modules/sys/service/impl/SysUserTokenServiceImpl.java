package io.java.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.java.common.utils.R;
import io.java.modules.sys.dao.SysUserTokenDao;
import io.java.modules.sys.entity.SysUserTokenEntity;
import io.java.modules.sys.jwt.TokenGenerator;
import io.java.modules.sys.service.SysUserTokenService;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
	//Expires in 12 hours
	private final static int EXPIRE = 3600 * 12;


	@Override
	public R createToken(long userId) {
		//Generate a token
		String token = TokenGenerator.generateValue();

        //Current time
		Date now = new Date();
        //Expiration time
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

       //Judge whether a token has been generated
		SysUserTokenEntity tokenEntity = this.getById(userId);
		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//Save token
			this.save(tokenEntity);
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//Update token
			this.updateById(tokenEntity);
		}

		R r = R.ok().put("token", token).put("expire", EXPIRE);

		return r;
	}

	@Override
	public void logout(long userId) {
		//Generate a token
		String token = TokenGenerator.generateValue();

		//Edittoken
		SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		this.updateById(tokenEntity);
	}
}
