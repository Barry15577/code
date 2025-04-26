package io.java.modules.sys.jwt;

import io.java.modules.sys.entity.SysUserEntity;
import io.java.modules.sys.entity.SysUserTokenEntity;
import io.java.modules.sys.service.ShiroService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Authentication
 *
 * @author Jiasi Lu
 */
@Component
public class JWTRealm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * Authorization (called when verifying permissions)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //User permission list
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * Authentication (called when logging in)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //Query user information based on accessToken
        SysUserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
        //token expired
        if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
            throw new IncorrectCredentialsException("token expired, please log in again");
        }

        //Query user information
        SysUserEntity user = shiroService.queryUser(tokenEntity.getUserId());
        //Account locked
        if(user.getStatus() == 0){
            throw new LockedAccountException("Account has been locked, please contact the administrator");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }
}
