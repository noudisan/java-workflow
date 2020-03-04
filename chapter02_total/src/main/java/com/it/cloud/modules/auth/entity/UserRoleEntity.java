package com.it.cloud.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.it.cloud.common.validator.group.AddGroup;
import com.it.cloud.common.validator.group.UpdateGroup;
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
@TableName("IT_USER_ROLE")
public class UserRoleEntity implements Serializable {


    @TableId(value = "ID")
    private String id;

    @NotBlank(message = "用户Id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @TableField("USER_ID")
    private String userId;

    @NotBlank(message = "角色Id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @TableField("ROLE_ID")
    private String roleId;


}
