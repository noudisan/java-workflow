package com.it.cloud.modules.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.it.cloud.common.validator.group.AddGroup;
import com.it.cloud.common.validator.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

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
@TableName("IT_USER")
public class UserEntity implements Serializable {


    @TableId(value = "ID")
    private String id;

    @NotBlank(message = "用户名不能为空", groups = AddGroup.class)
    @TableField("USERNAME")
    private String username;

    @NotBlank(message = "密码不能为空", groups = AddGroup.class)
    @TableField("PASSWORD")
    private String password;

    @TableField("SALT")
    private String salt;

    @TableField("SEX")
    private String sex;

    @TableField("AGE")
    private Integer age;

    @Email
    @TableField("EMAIL")
    private String email;

    @Pattern(regexp = "^[150[0-9]+]{11}")
    @TableField("MOBILE")
    private String mobile;

    @TableField("STATUS")
    private Integer status;

    @TableField("CREATE_USER")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Timestamp createTime;

    @NotBlank(message = "更新用户名不能为空", groups = UpdateGroup.class)
    @TableField("UPDATE_USER")
    private String updateUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Timestamp updateTime;

    @TableField("DELETE_STATUS")
    private Integer deleteStatus;

    @TableField(exist = false)
    private List<RoleEntity> roleList;
}
