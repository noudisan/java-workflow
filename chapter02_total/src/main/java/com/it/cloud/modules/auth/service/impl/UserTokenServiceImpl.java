package com.it.cloud.modules.auth.service.impl;

import com.it.cloud.common.base.Result;
import com.it.cloud.common.constants.RedisKeyConstants;
import com.it.cloud.common.utils.RedisUtils;
import com.it.cloud.modules.auth.entity.UserTokenEntity;
import com.it.cloud.modules.auth.oauth2.TokenGenerator;
import com.it.cloud.modules.auth.service.IUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * SysUserTokenServiceImpl
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Service
public class UserTokenServiceImpl implements IUserTokenService {

    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 生成Token
     *
     * @param userId
     * @return
     */
    @Override
    public Result createToken(String userId) {
        // 生成一个token
        String token = TokenGenerator.generateValue();

        String tokenKey = RedisKeyConstants.MANAGE_SYS_USER_TOKEN + token;
        String userIdKey = RedisKeyConstants.MANAGE_SYS_USER_TOKEN + userId;

        //判断是否生成过token
        String tokenInRedis = redisUtils.get(userIdKey);
        if (!StringUtils.isEmpty(tokenInRedis)) {
            // 将原来的token删除
            redisUtils.delete(RedisKeyConstants.MANAGE_SYS_USER_TOKEN + tokenInRedis);
        }
        // 将token存进redis
        redisUtils.set(tokenKey, userId, EXPIRE);
        redisUtils.set(userIdKey, token, EXPIRE);

        return Result.ok(token);
    }

    /**
     * 查询token
     *
     * @param token
     * @return
     */
    @Override
    public UserTokenEntity queryByToken(String token) {
        String userId = redisUtils.get(token);
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        UserTokenEntity sysUserToken = new UserTokenEntity();
        sysUserToken.setToken(token);
        sysUserToken.setUserId(userId);
        return sysUserToken;
    }

    /**
     * 退出登录
     *
     * @param userId
     */
    @Override
    public void logout(String userId) {
        String userIdKey = RedisKeyConstants.MANAGE_SYS_USER_TOKEN + userId;
        String token = redisUtils.get(userIdKey);
        String tokenKey = RedisKeyConstants.MANAGE_SYS_USER_TOKEN + token;
        redisUtils.delete(userIdKey);
        redisUtils.delete(tokenKey);
    }

    /**
     * 续期
     *
     * @param userId
     * @param token
     */
    @Override
    public void refreshToken(String userId, String token) {
        String tokenKey = RedisKeyConstants.MANAGE_SYS_USER_TOKEN + token;
        String userIdKey = RedisKeyConstants.MANAGE_SYS_USER_TOKEN + userId;
        redisUtils.updateExpire(tokenKey);
        redisUtils.updateExpire(userIdKey);
    }

}
