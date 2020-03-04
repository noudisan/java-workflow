package com.it.cloud.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.it.cloud.common.validator.group.AddGroup;
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
@TableName("IT_ROLE")
public class RoleEntity implements Serializable {


    @TableId(value = "ID")
    private String id;

    @NotBlank(message = "角色名不能为空", groups = AddGroup.class)
    @TableField("ROLE_NAME")
    private String roleName;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_USER")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Timestamp createTime;

    @TableField(exist = false)
    private List<MenuEntity> menuList;

}
