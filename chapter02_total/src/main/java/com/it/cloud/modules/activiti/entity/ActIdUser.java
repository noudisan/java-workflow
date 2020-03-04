package com.it.cloud.modules.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ACT_ID_USER")
public class ActIdUser implements Serializable {


    @TableId(value = "ID_")
    private String id;

    @TableField("REV_")
    private Integer rev;

    @TableField("FIRST_")
    private String first;

    @TableField("LAST_")
    private String last;

    @TableField("EMAIL_")
    private String email;

    @TableField("PWD_")
    private String pwd;

    @TableField("PICTURE_ID_")
    private String pictureId;


}
