package com.it.cloud.modules.auth.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  token实体
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@ApiModel(value = "UserToken对象", description = "用户Token")
@Data
public class UserTokenEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "Token")
    private String token;

}
