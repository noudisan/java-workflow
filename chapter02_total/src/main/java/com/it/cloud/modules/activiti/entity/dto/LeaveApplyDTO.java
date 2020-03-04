package com.it.cloud.modules.activiti.entity.dto;

import com.it.cloud.common.validator.group.AddGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author 司马缸砸缸了
 * @description 提交申请
 * @date 2019/7/27 19:05
 */
@Data
public class LeaveApplyDTO implements Serializable {

    @ApiModelProperty(value = "请假单Id", name = "请假单Id", required = true)
    @NotBlank(message = "请假单Id不能为空", groups = AddGroup.class)
    private String id;

    @ApiModelProperty(value = "申批人", name = "users", required = true)
    private String users;

    @ApiModelProperty(value = "申批人ID集合", name = "userList", required = true)
    private List<String> userList;
}
