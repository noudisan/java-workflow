package com.it.cloud.modules.activiti.controller;

import cn.hutool.json.JSONUtil;
import com.it.cloud.common.annotation.SysLog;
import com.it.cloud.common.base.Result;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.modules.activiti.entity.ActReModelEntity;
import com.it.cloud.modules.activiti.service.IActReModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 模型控制类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Api(value = "模型控制器", tags = "模型")
@Slf4j
@RestController
@RequestMapping("/act/model")
public class ActReModelController {

    @Autowired
    private IActReModelService actReModelService;

    @ApiOperation(value = "分页查询接口", notes = "条件，分页查询")
    @GetMapping("/page")
    public Result list(@RequestParam Map<String, Object> params) {
        log.info("分页查询所有流程模型接口，参数:{}", JSONUtil.toJsonStr(params));
        PageUtils page = actReModelService.queryPage(params);

        return Result.ok(page);
    }

    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增流程模型")
    @PostMapping("")
    @RequiresPermissions("act:model:save")
    public Result save(@RequestBody ActReModelEntity actReModel) {
        String modelId = actReModelService.save(actReModel);

        return Result.ok(modelId);
    }

    @ApiOperation(value = "部署", notes = "部署")
    @SysLog("部署流程文件")
    @GetMapping("/deploy")
    @RequiresPermissions("act:model:deploy")
    public Result deploy(String id) {
        actReModelService.deploy(id);

        return Result.ok();
    }

    @ApiOperation(value = "导出model的xml文件", notes = "导出model的xml文件")
    @GetMapping("/export")
    public void export(String id, @RequestParam(defaultValue = "xml", required = false) String type,
                       HttpServletResponse response) {
        actReModelService.export(id, type, response);
    }

    @ApiOperation(value = "批量删除", notes = "批量删除")
    @SysLog("删除")
    @PostMapping("/delete")
    @RequiresPermissions("act:model:delete")
    public Result delete(@RequestBody String[] ids) {
        actReModelService.deleteBatch(ids);

        return Result.ok();
    }
}
