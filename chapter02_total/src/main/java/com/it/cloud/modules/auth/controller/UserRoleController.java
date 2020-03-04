package com.it.cloud.modules.auth.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.it.cloud.common.annotation.SysLog;
import com.it.cloud.common.base.Result;
import com.it.cloud.modules.auth.entity.dto.UserRoleDTO;
import com.it.cloud.modules.auth.service.IUserRoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@RestController
@Slf4j
@RequestMapping("/admin/userRole")
public class UserRoleController {

    @Autowired
    private IUserRoleService userRoleService;

    @ApiOperation(value = "分配角色", notes = "分配角色")
    @SysLog("分配角色")
    @PostMapping("/allot")
    @RequiresRoles(value={"super_admin", "admin"}, logical= Logical.OR)
    public Result allot(@ApiParam(value = "UserRoleDTO实体类", required = true)
                        @RequestBody UserRoleDTO userRoleDTO) {
        log.info("分配角色参数:{}", JSONUtil.toJsonStr(userRoleDTO));

        if (StringUtils.isBlank(userRoleDTO.getUserId()) ||
                CollectionUtil.isEmpty(userRoleDTO.getRoleList())) {
            return Result.error("用户Id或者角色列表为空");
        }
        userRoleService.allotRole(userRoleDTO);
        return Result.ok();
    }

}
