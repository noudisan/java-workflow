package com.it.cloud.modules.activiti.controller;


import cn.hutool.json.JSONUtil;
import com.it.cloud.common.annotation.SysLog;
import com.it.cloud.common.base.Result;
import com.it.cloud.common.exceptions.YYException;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.modules.activiti.service.IActReProcdefService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 * 流程定义控制类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Api(value = "流程定义控制器", tags = "流程定义")
@Slf4j
@RestController
@RequestMapping("/act/procdef")
public class ActReProcdefController {


    @Autowired
    private IActReProcdefService actReProcdefService;

    @ApiOperation(value = "分页查询接口", notes = "条件，分页查询")
    @GetMapping("/page")
    public Result list(@RequestParam Map<String, Object> params) {
        log.info("分页查询所有流程定义接口，参数:{}", JSONUtil.toJsonStr(params));
        PageUtils page = actReProcdefService.queryPage(params);
        System.out.println(JSONUtil.toJsonStr(page));
        return Result.ok(page);
    }

    @ApiOperation(value = "读取资源", notes = "读取资源, 通过ProcessDefinitionId")
    @GetMapping("/read")
    public void resourceRead(String id,
                             @RequestParam(required = false) String proInsId,
                             String type,
                             HttpServletResponse response) {
        log.info("读取资源, processDefinitionId:{}, proInsId:{}, type:{}", id, proInsId, type);
        InputStream resourceAsStream = actReProcdefService.readResource(id, proInsId, type);
        byte[] b = new byte[1024];
        int len = -1;
        int lenEnd = 1024;
        while (true) {
            try {
                if (!((len = resourceAsStream.read(b, 0, lenEnd)) != -1)) break;
                response.getOutputStream().write(b, 0, len);
            } catch (IOException e) {
                throw new YYException("读取资源文件失败", e);
            }
        }
    }

    @ApiOperation(value = "部署流程文件", notes = "部署流程文件")
    @SysLog("部署流程文件")
    @PostMapping("/deploy")
    @RequiresPermissions("act:reprocdef:deploy")
    public Result deploy(MultipartFile file) {
        Result result = Result.ok();
        String exportDir = this.getClass().getResource("/").getPath();
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new YYException("请选择要部署的流程文件");
        } else {
            result = actReProcdefService.deploy(exportDir, file);
        }
        return result;
    }

    @ApiOperation(value = "转模型", notes = "转模型")
    @SysLog("转模型")
    @GetMapping("/convertToModel")
    @RequiresPermissions("act:reprocdef:convertToModel")
    public Result convertToModel(String id) {
        log.info("转模型, processDefinitionId:{}", id);
        try {
            actReProcdefService.convertToModel(id);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.ok();
    }

    @ApiOperation(value = "启动流程实例", notes = "启动流程实例，通过processDefinitionId")
    @SysLog("启动流程实例")
    @GetMapping("/startProcessInstance")
    @RequiresPermissions("act:reprocdef:start")
    public Result startProcessInstanceById(String processDefinitionId) {
        log.info("启动流程实例, processDefinitionId:{}", processDefinitionId);
        actReProcdefService.startProcessInstanceById(processDefinitionId);

        return Result.ok();
    }

    @ApiOperation(value = "激活/挂起", notes = "激活/挂起")
    @SysLog("激活/挂起")
    @PutMapping("/status")
    @RequiresPermissions("act:reprocdef:update")
    public Result update(String id, Integer state) {
        log.info("激活/挂起流程定义, id:{}, state:{}", id, state);
        Result result = actReProcdefService.updateState(id, state);

        return result;
    }

    @ApiOperation(value = "删除流程定义", notes = "删除流程定义(部署)")
    @SysLog("删除流程定义")
    @PostMapping("/delete")
    @RequiresPermissions("act:reprocdef:delete")
    public Result delete(@RequestBody String[] deploymentIds) {
        log.info("删除流程定义, 参数:{}", JSONUtil.toJsonStr(deploymentIds));
        actReProcdefService.deleteBatch(deploymentIds);

        return Result.ok();
    }
}
