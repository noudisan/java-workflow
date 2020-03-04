package com.it.cloud.modules.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ACT_RE_PROCDEF")
public class ActReProcdefEntity implements Serializable {


    @ApiModelProperty(value = "流程ID，由《流程编号：流程版本号：自增长ID》组成")
    @TableId(value = "ID_")
    private String id;

    @ApiModelProperty(value = "乐观锁版本号")
    @TableField("REV_")
    private Integer rev;

    @ApiModelProperty(value = "流程命名空间（该编号就是流程文件targetNamespace的属性值）")
    @TableField("CATEGORY_")
    private String category;

    @ApiModelProperty(value = "流程名称（该编号就是流程文件process元素的name属性值）")
    @TableField("NAME_")
    private String name;

    @ApiModelProperty(value = "流程编号（该编号就是流程文件process元素的id属性值）")
    @TableField("KEY_")
    private String key;

    @ApiModelProperty(value = "流程版本号（由程序控制，新增即为1，修改后依次加1来完成的）")
    @TableField("VERSION_")
    private Integer version;

    @ApiModelProperty(value = "部署编号")
    @TableField("DEPLOYMENT_ID_")
    private String deploymentId;

    @ApiModelProperty(value = "资源文件名称")
    @TableField("RESOURCE_NAME_")
    private String resourceName;

    @ApiModelProperty(value = "图片资源文件名称")
    @TableField("DGRM_RESOURCE_NAME_")
    private String dgrmResourceName;

    @ApiModelProperty(value = "描述信息")
    @TableField("DESCRIPTION_")
    private String description;

    @ApiModelProperty(value = "是否从key启动，start节点是否存在formKey，0否  1是")
    @TableField("HAS_START_FORM_KEY_")
    private String hasStartFormKey;

    @ApiModelProperty(value = "是否有图片预览")
    @TableField("HAS_GRAPHICAL_NOTATION_")
    private String hasGraphicalNotation;

    @ApiModelProperty(value = "是否挂起，1激活 2挂起")
    @TableField("SUSPENSION_STATE_")
    private Integer suspensionState;

    @ApiModelProperty(value = "所属系统")
    @TableField("TENANT_ID_")
    private String tenantId;

    @ApiModelProperty(value = "工作流引擎版本")
    @TableField("ENGINE_VERSION_")
    private String engineVersion;

    @ApiModelProperty(value = "部署时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Timestamp deployTime;

}
