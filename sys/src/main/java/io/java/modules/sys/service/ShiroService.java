

package io.java.modules.sys.service;

import io.java.modules.sys.entity.SysUserEntity;
import io.java.modules.sys.entity.SysUserTokenEntity;

import java.util.Set;

/**
 * Shiro related interfaces
 *
 * @author Jiasi Lu
 */
public interface ShiroService {
    /**
     * Get user permission list
     */
    Set<String> getUserPermissions(long userId);

    SysUserTokenEntity queryByToken(String token);

    /**
     * Query user based on user ID
     * @param userId
     */
    SysUserEntity queryUser(Long userId);
}