package com.it.cloud.common.base;

import com.it.cloud.common.constants.SysConstants;
import com.it.cloud.modules.auth.entity.UserEntity;
import org.apache.shiro.SecurityUtils;

/**
 * controller基础类
 *
 * @author 司马缸砸缸了
 * @version 1.0
 * @since 2019-07-12
 */
public class BaseController {

    protected UserEntity getUser() {
        return (UserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    protected String getUserId() {
        if(getUser() == null){
            return SysConstants.SUPER_ADMIN;
        }
        return getUser().getId();
    }

    protected String getUsername() {
        if(getUser() == null){
            return SysConstants.SUPER_ADMIN_NAME;
        }
        return getUser().getUsername();
    }
}
