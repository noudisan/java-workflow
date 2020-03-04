package com.it.cloud.modules.auth.entity.vo;

import com.it.cloud.modules.auth.entity.MenuEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author 司马缸砸缸了
 * @description 导航
 * @date 2019/7/27 21:34
 */
@ApiModel(value = "NavVo", description = "导航返回值")
@Data
@AllArgsConstructor
public class NavVo implements Serializable {

    @ApiModelProperty(value = "目录-菜单")
    private List<MenuEntity> menuList;

    @ApiModelProperty(value = "权限集合")
    private Set<String> permissions;
}
