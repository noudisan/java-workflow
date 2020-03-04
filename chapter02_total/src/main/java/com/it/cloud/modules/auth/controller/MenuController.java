package com.it.cloud.modules.auth.controller;


import com.it.cloud.common.annotation.SysLog;
import com.it.cloud.common.base.BaseController;
import com.it.cloud.common.base.Result;
import com.it.cloud.common.enums.MenuTypeEnum;
import com.it.cloud.common.exceptions.YYException;
import com.it.cloud.common.validator.ValidatorUtils;
import com.it.cloud.modules.auth.entity.MenuEntity;
import com.it.cloud.modules.auth.entity.RoleEntity;
import com.it.cloud.modules.auth.entity.vo.NavVo;
import com.it.cloud.modules.auth.service.IMenuService;
import com.it.cloud.modules.auth.service.IRoleMenuService;
import com.it.cloud.modules.auth.service.IShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */

@Api(value = "菜单控制器", tags = "菜单")
@Slf4j
@RestController
@RequestMapping("/admin/menu")
public class MenuController extends BaseController {

    @Autowired
    private IMenuService menuService;
    @Autowired
    private IShiroService shiroService;
    @Autowired
    private IRoleMenuService roleMenuService;

    @ApiOperation(value = "导航菜单", notes = "导航菜单")
    @GetMapping("/nav")
    public Result nav() {
        //目录-菜单列表
        List<MenuEntity> menuList = menuService.getUserMenuList(getUserId());
        //权限集合
        Set<String> permissions = shiroService.getUserPermissions(getUserId());

        return Result.ok(new NavVo(menuList, permissions));
    }

    @ApiOperation(value = "所有菜单列表", notes = "所有菜单列表")
    @GetMapping("/list")
    public Result list(MenuEntity menuEntity) {
        List<MenuEntity> menuList = menuService.queryList(menuEntity);

        return Result.ok(menuList);
    }

    @ApiOperation(value = "选择菜单(添加、修改菜单)", notes = "选择菜单(添加、修改菜单)")
    @GetMapping("/select")
    public Result select() {
        //查询列表数据
        List<MenuEntity> menuList = menuService.queryNotButtonList();

        return Result.ok(menuList);
    }

    @ApiOperation(value = "角色菜单列表", notes = "角色菜单列表")
    @GetMapping("/role/list")
    public Result listByRoleId(@RequestParam String roleId) {
        if (StringUtils.isBlank(roleId)) {
            return Result.error("roleId不能为空");
        }
        List<MenuEntity> list = roleMenuService.queryByRoleId(roleId);
        // 过滤，只要按钮级别的menu
        list = list.stream().filter(item -> item.getType() == 2).collect(Collectors.toList());

        return Result.ok(list);
    }

    @ApiOperation(value = "菜单信息", notes = "菜单信息")
    @GetMapping("/{id}")
    public Result info(@PathVariable("id") String id) {
        MenuEntity menu = menuService.getById(id);

        return Result.ok(menu);
    }

    @ApiOperation(value = "保存", notes = "保存")
    @SysLog("保存菜单")
    @PostMapping("")
    @RequiresPermissions("sys:menu:save")
    public Result save(@RequestBody MenuEntity menu) {
        //数据校验
        ValidatorUtils.validateEntity(menu);
        verifyForm(menu);

        menuService.save(menu);

        return Result.ok();
    }

    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改菜单")
    @PutMapping("")
    @RequiresPermissions("sys:menu:update")
    public Result update(@RequestBody MenuEntity menu) {
        //数据校验
        ValidatorUtils.validateEntity(menu);
        verifyForm(menu);

        menuService.updateById(menu);

        return Result.ok();
    }

    @ApiOperation(value = "删除", notes = "删除")
    @SysLog("删除菜单")
    @DeleteMapping("/{id}")
    @RequiresPermissions("sys:menu:delete")
    public Result delete(@PathVariable("id") String id) {
        //判断是否有子菜单或按钮
        List<MenuEntity> menuList = menuService.queryListByParentId(id);
        if (menuList.size() > 0) {
            return Result.error("请先删除子菜单或按钮");
        }

        menuService.delete(id);

        return Result.ok();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(MenuEntity menu) {
        //菜单
        if (menu.getType() == MenuTypeEnum.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new YYException("菜单URL不能为空");
            }
        }

        //上级菜单类型, 不是目录就必须存在上级菜单
        int parentType = MenuTypeEnum.CATALOG.getValue();
        if (!"0".equals(menu.getParentId())) {
            MenuEntity parentMenu = menuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == MenuTypeEnum.CATALOG.getValue() ||
                menu.getType() == MenuTypeEnum.MENU.getValue()) {
            if (parentType != MenuTypeEnum.CATALOG.getValue()) {
                throw new YYException("上级菜单只能为目录类型");
            }
        }

        //按钮
        if (menu.getType() == MenuTypeEnum.BUTTON.getValue()) {
            if (parentType != MenuTypeEnum.MENU.getValue()) {
                throw new YYException("上级菜单只能为菜单类型");
            }
        }
    }
}
