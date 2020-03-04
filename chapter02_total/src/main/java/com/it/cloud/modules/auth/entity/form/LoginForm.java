package com.it.cloud.modules.auth.entity.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  登陆控制器
 * </p>
 *
 * @author 登录表单对象
 * @since 2019-07-24
 */

@ApiModel(value = "登录表单对象", description = "登录表单对象")
@Data
public class LoginForm {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String captcha;

    @ApiModelProperty(value = "生成验证码的UUID")
    private String uuid;
}
