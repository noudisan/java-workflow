package com.it.cloud.modules.auth.oauth2;

import com.it.cloud.modules.auth.entity.UserEntity;
import com.it.cloud.modules.auth.entity.UserTokenEntity;
import com.it.cloud.modules.auth.service.IShiroService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Set;

/**
 * <p>
 *  认证，授权
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private IShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 第六步
     */
    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserEntity user = (UserEntity) principals.getPrimaryPrincipal();
        String userId = user.getId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);
        //用户角色列表
        Set<String> roleSet = shiroService.getUserRoles(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        info.setRoles(roleSet);
        return info;
    }

    /**
     * 第五步
     */
    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息
        UserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
        //token失效
        if (tokenEntity == null) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        UserEntity user = shiroService.queryUser(tokenEntity.getUserId());
        //账号锁定
        if (user.getStatus() != 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // 续期
        shiroService.refreshToken(tokenEntity.getUserId(), accessToken);

        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }

    /**
     * 清除当前用户的权限缓存
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

}
