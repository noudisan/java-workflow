package com.it.cloud.modules.auth.entity.dto;

import com.it.cloud.common.validator.group.AddGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author 司马缸砸缸了
 * @description 角色-菜单关系接收类
 * @date 2019/7/27 19:05
 */
@Data
public class RoleMenuDTO {

    @ApiModelProperty(value = "角色Id", name = "角色Id", required = true)
    @NotBlank(message = "角色Id不能为空", groups = AddGroup.class)
    private String roleId;


    @ApiModelProperty(value = "菜单ID集合", name = "menuList", required = true)
    private List<String> menuList;
}
