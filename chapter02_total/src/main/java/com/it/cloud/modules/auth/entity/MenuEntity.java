package com.it.cloud.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.it.cloud.common.validator.group.AddGroup;
import com.it.cloud.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("IT_MENU")
public class MenuEntity implements Serializable {


    @TableId(value = "ID")
    private String id;

    @ApiModelProperty(value = "父菜单ID，一级菜单为0")
    @NotBlank(message = "父ID不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @TableField("PARENT_ID")
    private String parentId;

    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "菜单URL")
    @TableField("URL")
    private String url;

    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list,user:create)", notes = "更新的时候忽略非空判断")
    @TableField(value = "PERMS",updateStrategy = FieldStrategy.IGNORED)
    private String perms;

    @ApiModelProperty(value = "类型   0：目录   1：菜单   2：按钮")
    @NotBlank(message = "类型不能为空", groups = AddGroup.class)
    @TableField("TYPE")
    private Integer type;

    @ApiModelProperty(value = "菜单图标")
    @TableField("ICON")
    private String icon;

    @ApiModelProperty(value = "排序")
    @TableField("ORDER_NUM")
    private Integer orderNum;

    @TableField("CREATE_USER")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Timestamp createTime;


    @ApiModelProperty(value = "父菜单名称")
    @TableField(exist=false)
    private String parentName;

    @ApiModelProperty(value = "z-tree属性")
    @TableField(exist=false)
    private Boolean open;

    @ApiModelProperty(value = "目录-菜单集合")
    @TableField(exist=false)
    private List<?> list;

}
