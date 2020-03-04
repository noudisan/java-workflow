package com.it.cloud.modules.auth.controller;


import cn.hutool.json.JSONUtil;
import com.it.cloud.common.base.BaseController;
import com.it.cloud.common.base.Result;
import com.it.cloud.common.constants.SysConstants;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.common.validator.ValidatorUtils;
import com.it.cloud.modules.auth.entity.MenuEntity;
import com.it.cloud.modules.auth.entity.RoleEntity;
import com.it.cloud.modules.auth.entity.dto.RoleDTO;
import com.it.cloud.modules.auth.service.IRoleMenuService;
import com.it.cloud.modules.auth.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Api(value = "角色控制器", tags = "角色")
@Slf4j
@RestController
@RequestMapping("/admin/role")
public class RoleController extends BaseController {
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IRoleMenuService roleMenuService;


    @ApiOperation(value = "分页查询角色接口", notes = "条件，分页查询")
    @GetMapping("")
    public Result page(@RequestParam Map<String, Object> params) {
        //如果不是超级管理员，则只查询自己创建的角色列表
        if (!SysConstants.SUPER_ADMIN.equals(getUserId())) {
            params.put("createUser", getUserId());
        }

        PageUtils page = roleService.queryPage(params);

        return Result.ok(page);
    }

    @ApiOperation(value = "角色列表", notes = "角色列表")
    @GetMapping("/list")
    public Result list() {
        Map<String, Object> map = new HashMap<>(1);

        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if (!SysConstants.SUPER_ADMIN.equals(getUserId())) {
            map.put("create_user", getUserId());
        }
        Collection<RoleEntity> list = roleService.listByMap(map);

        return Result.ok(list);
    }

    @ApiOperation(value = "用户角色列表", notes = "用户角色列表")
    @GetMapping("/user/list")
    public Result listByUser(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return Result.error("userId 不能为空");
        }
        List<RoleEntity> list = roleService.queryByUser(userId);

        return Result.ok(list);
    }

    @ApiOperation(value = "获取角色信息", notes = "获取角色信息")
    @GetMapping("/{id}")
    public Result info(@PathVariable String id) {
        RoleEntity role = roleService.getById(id);
        List<MenuEntity> menuList = roleMenuService.queryByRoleId(id);
        role.setMenuList(menuList);

        return Result.ok(role);
    }

    @ApiOperation(value = "保存角色信息", notes = "保存角色信息")
    @PostMapping("")
    @RequiresPermissions("sys:role:save")
    public Result save(@RequestBody RoleDTO roleDto) {
        ValidatorUtils.validateEntity(roleDto);

        roleDto.setCreateUser(getUserId());
        roleService.save(roleDto);

        return Result.ok();
    }

    @ApiOperation(value = "更新角色", notes = "更新角色")
    @PutMapping("")
    @RequiresPermissions("sys:role:update")
    public Result update(@RequestBody RoleDTO roleDto) {
        ValidatorUtils.validateEntity(roleDto);

        roleDto.setCreateUser(getUserId());
        roleService.updateById(roleDto);

        return Result.ok();
    }

    @ApiOperation(value = "删除角色信息", notes = "删除角色信息")
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public Result delete(@RequestBody String[] ids) {
        log.info("删除角色，Ids:{}", JSONUtil.toJsonStr(ids));
        roleService.deleteBatch(ids);

        return Result.ok();
    }
}
