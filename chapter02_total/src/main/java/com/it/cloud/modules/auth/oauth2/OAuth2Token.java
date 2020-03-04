package com.it.cloud.modules.auth.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * <p>
 *  token实现
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
