package com.it.cloud.modules.auth.service;

import com.it.cloud.modules.auth.entity.UserEntity;
import com.it.cloud.modules.auth.entity.UserTokenEntity;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
public interface IShiroService {

    /**
     * 获取用户的所有权限
     *
     * @param userId
     * @return
     */
    Set<String> getUserPermissions(String userId);

    /**
     * 获取用户的所有角色
     *
     * @param userId
     * @return
     */
    Set<String> getUserRoles(String userId);

    /**
     * 查询token
     *
     * @param token
     * @return
     */
    UserTokenEntity queryByToken(String token);

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    UserEntity queryUser(String userId);

    /**
     * 续期
     *
     * @param userId
     * @param accessToken
     */
    void refreshToken(String userId, String accessToken);
}
