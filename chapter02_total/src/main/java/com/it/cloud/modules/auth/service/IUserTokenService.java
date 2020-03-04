package com.it.cloud.modules.auth.service;

import com.it.cloud.common.base.Result;
import com.it.cloud.modules.auth.entity.UserTokenEntity;

/**
 * <p>
 *  userToken
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
public interface IUserTokenService {
    /**
     * 生成Token
     *
     * @param userId
     * @return
     */
    Result createToken(String userId);

    /**
     * 查询token
     *
     * @param token
     * @return
     */
    UserTokenEntity queryByToken(String token);

    /**
     * 退出登录
     *
     * @param userId
     */
    void logout(String userId);

    /**
     * 续期
     *
     * @param userId
     * @param token
     */
    void refreshToken(String userId, String token);
}
