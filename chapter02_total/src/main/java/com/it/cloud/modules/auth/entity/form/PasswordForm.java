package com.it.cloud.modules.auth.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  登陆控制器
 * </p>
 *
 * @author 密码对象
 * @since 2019-07-24
 */

@ApiModel(value = "密码对象", description = "密码对象")
@Data
public class PasswordForm {

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "新密码")
    private String newPassword;

}
