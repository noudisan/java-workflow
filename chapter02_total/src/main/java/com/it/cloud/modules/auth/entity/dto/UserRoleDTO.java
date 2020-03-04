package com.it.cloud.modules.auth.entity.dto;

import com.it.cloud.common.validator.group.AddGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author 司马缸砸缸了
 * @description 用户-角色关系接收类
 * @date 2019/7/27 19:05
 */
@Data
public class UserRoleDTO{

    @ApiModelProperty(value = "用户Id", name = "用户Id", required = true)
    @NotBlank(message = "用户Id不能为空", groups = AddGroup.class)
    private String userId;


    @ApiModelProperty(value = "角色ID集合", name = "roleList", required = true)
    private List<String> roleList;
}
