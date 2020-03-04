package com.it.cloud.modules.auth.controller;


import cn.hutool.json.JSONUtil;
import com.it.cloud.common.annotation.SysLog;
import com.it.cloud.common.base.Result;
import com.it.cloud.common.constants.SysConstants;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.common.validator.ValidatorUtils;
import com.it.cloud.common.validator.group.AddGroup;
import com.it.cloud.modules.auth.entity.UserEntity;
import com.it.cloud.modules.auth.service.IUserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-15
 */
@Api(value = "用户控制器", tags = "用户")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @ApiOperation(value = "分页查询用户接口", notes = "条件，分页查询")
    @GetMapping("")
    public Result getPage(@ApiParam(value = "请求过滤参数") @RequestParam Map<String, Object> params) {
        log.info("分页查询所有用户接口，参数:{}", JSONUtil.toJsonStr(params));
        PageUtils page = userService.queryPage(params);

        return Result.ok(page);
    }

    @ApiOperation(value = "条件查询用户接口", notes = "条件，不分页查询")
    @GetMapping("/list")
    public Result getList(UserEntity userEntity) {
        log.info("查询所有用户接口，参数:{}", JSONUtil.toJsonStr(userEntity));
        List<UserEntity> list = userService.queryList(userEntity);

        return Result.ok(list);
    }

    @ApiOperation(value = "ID查询用户", notes = "根据ID查询用户")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "主键Id", required = true, dataType = "string"),})
    @GetMapping("/{id}")
    public Result getUser(@PathVariable String id) {
        log.info("ID查询用户参数:{}", id);
        UserEntity user = userService.getById(id);

        return Result.ok(user);
    }

    @ApiOperation(value = "添加用户", notes = "添加用户")
    @SysLog("添加用户")
    @PostMapping("")
    public Result save(@ApiParam(value = "UserEntity实体类", required = true)
                       @RequestBody UserEntity userEntity) {
        log.info("添加用户参数:{}", JSONUtil.toJsonStr(userEntity));
        ValidatorUtils.validateEntity(userEntity, AddGroup.class);

        // 加密密码
        String password = new Sha256Hash(userEntity.getPassword(), SysConstants.PASSWORD_SALT).toHex();
        userEntity.setPassword(password);
        userEntity.setSalt(SysConstants.PASSWORD_SALT);
        userService.save(userEntity);

        return Result.ok();
    }

    @ApiOperation(value = "更新用户", notes = "更新用户")
    @SysLog("更新用户")
    @PutMapping("")
    @RequiresPermissions("sys:user:update")
    public Result update(@RequestBody UserEntity userEntity) {
        log.info("更新用户参数:{}", JSONUtil.toJsonStr(userEntity));
        // TODO:修改密码是个bug，查询的时候没解码
        userService.updateById(userEntity);

        return Result.ok();
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @SysLog("删除用户")
    @DeleteMapping("/{id}")
    @RequiresPermissions("sys:user:delete")
    public Result delete(@ApiParam(value = "ID主键", required = true) @PathVariable String id) {
        log.info("删除用户,id:{}", id);
        userService.removeById(id);

        return Result.ok();
    }

}
