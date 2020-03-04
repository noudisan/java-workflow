package com.it.cloud.modules.auth.entity.dto;

import com.it.cloud.modules.auth.entity.RoleEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 司马缸砸缸了
 * @description 角色菜单关系接收类
 * @date 2019/7/27 19:05
 */
@Data
public class RoleDTO extends RoleEntity {

    @ApiModelProperty(value = "菜单ID集合", name = "menuIdList", required = false)
    private List<String> menuIdList;
}
