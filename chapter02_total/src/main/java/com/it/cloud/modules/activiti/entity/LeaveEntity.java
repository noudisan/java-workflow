package com.it.cloud.modules.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.it.cloud.common.validator.group.AddGroup;
import com.it.cloud.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("IT_LEAVE")
public class LeaveEntity implements Serializable {


    @TableId(value = "ID")
    private String id;

    @TableField("PROCESS_INSTANCE_ID")
    private String processInstanceId;

    @NotBlank(message = "标题不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @TableField("TITLE")
    private String title;

    @NotBlank(message = "用户ID不能为空", groups = AddGroup.class)
    @TableField("USER_ID")
    private String userId;

    @TableField("USER_NAME")
    private String userName;

    @TableField("DAYS")
    private Integer days;

    @NotNull(message = "开始时间不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("START_TIME")
    private Timestamp startTime;

    @NotNull(message = "结束时间不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("END_TIME")
    private Timestamp endTime;

    @NotBlank(message = "请假类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @TableField("LEAVE_TYPE")
    private String leaveType;

    @TableField("REASON")
    private String reason;

    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Timestamp createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Timestamp updateTime;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("SUBMIT_TIME")
    private Timestamp submitTime;

    @ApiModelProperty(value = "流程任务")
    @TableField(exist = false)
    private ActRuTaskEntity task;

    @ApiModelProperty(value = "流程任务集合")
    @TableField(exist = false)
    private List<ActRuTaskEntity> taskList;

    @ApiModelProperty(value = "流程实例")
    @TableField(exist = false)
    private ActRuExecution actRuExecution;

    @ApiModelProperty(value = "历史流程任务")
    @TableField(exist = false)
    private ActHiTaskinst actHiTaskinst;

    @ApiModelProperty(value = "历史流程实例")
    @TableField(exist = false)
    private ActHiProcinst actHiProcinst;
}
