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
@TableName("ACT_ID_MEMBERSHIP")
public class ActIdMembership implements Serializable {


    @TableId(value = "USER_ID_")
    private String userId;

    @TableField("GROUP_ID_")
    private String groupId;


}
