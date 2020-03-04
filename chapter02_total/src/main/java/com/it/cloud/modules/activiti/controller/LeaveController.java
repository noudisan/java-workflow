package com.it.cloud.modules.activiti.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.it.cloud.common.annotation.SysLog;
import com.it.cloud.common.base.BaseController;
import com.it.cloud.common.base.Result;
import com.it.cloud.common.constants.ActivitiConstant;
import com.it.cloud.common.exceptions.YYException;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.common.validator.ValidatorUtils;
import com.it.cloud.common.validator.group.AddGroup;
import com.it.cloud.common.validator.group.UpdateGroup;
import com.it.cloud.modules.activiti.entity.ActHiTaskinst;
import com.it.cloud.modules.activiti.entity.LeaveEntity;
import com.it.cloud.modules.activiti.entity.dto.LeaveApplyDTO;
import com.it.cloud.modules.activiti.entity.dto.LeaveOperateDTO;
import com.it.cloud.modules.activiti.service.IActReProcdefService;
import com.it.cloud.modules.activiti.service.ILeaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-08-28
 */
@Api(value = "请假控制器", tags = "请假")
@Slf4j
@RestController
@RequestMapping("/leave")
public class LeaveController extends BaseController {

    @Autowired
    private ILeaveService leaveService;

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IActReProcdefService actReProcdefService;

    @ApiOperation(value = "我的申请", notes = "我的申请")
    @GetMapping("/mine")
    public Result list(@RequestParam Map<String, Object> params) {
        log.info("分页查询我的申请定义接口，参数:{}", JSONUtil.toJsonStr(params));
        // 默认管理员方便测试
        String userId = getUserId();
        params.put("userId", userId);
        PageUtils page = leaveService.queryMyApplyPage(params);

        return Result.ok(page);
    }

    @ApiOperation(value = "工单详情", notes = "工单详情")
    @GetMapping("/{id}")
    public Result info(@PathVariable String id) {
        log.info("工单详情，id:{}", id);
        LeaveEntity leaveEntity = leaveService.getById(id);

        return Result.ok(leaveEntity);
    }

    @ApiOperation(value = "保存", notes = "保存")
    @PostMapping("")
    @RequiresPermissions("sys:act:save")
    public Result save(@RequestBody LeaveEntity leaveEntity) {
        log.info("添加leave定义接口，参数:{}", JSONUtil.toJsonStr(leaveEntity));
        // 默认管理员方便测试
        leaveEntity.setUserId(getUserId());
        leaveEntity.setUserName(getUsername());
        // 校验
        ValidatorUtils.validateEntity(leaveEntity, AddGroup.class);
        // 保存
        leaveService.saveLeave(leaveEntity);

        return Result.ok();
    }

    @ApiOperation(value = "更新", notes = "更新")
    @PutMapping("")
    @RequiresPermissions("sys:act:update")
    public Result update(@RequestBody LeaveEntity leaveEntity) {
        log.info("更新leave定义接口，参数:{}", JSONUtil.toJsonStr(leaveEntity));
        // 校验
        ValidatorUtils.validateEntity(leaveEntity, UpdateGroup.class);
        leaveEntity.setUpdateTime(DateUtil.date().toTimestamp());
        leaveService.updateById(leaveEntity);

        return Result.ok();
    }

    @ApiOperation(value = "删除请假单", notes = "删除请假单")
    @SysLog("删除请假单")
    @DeleteMapping("/{id}")
    @RequiresPermissions("sys:act:delete")
    public Result delete(@ApiParam(value = "ID主键", required = true) @PathVariable String id) {
        log.info("删除请假单,id:{}", id);
        leaveService.removeById(id);

        return Result.ok();
    }

    @ApiOperation(value = "提交申请", notes = "提交申请")
    @PostMapping("/apply")
    @RequiresPermissions("sys:act:apply")
    public Result apply(@RequestBody LeaveApplyDTO leaveApplyDTO) {
        log.info("提交申请接口，参数:{}", JSONUtil.toJsonStr(leaveApplyDTO));
        ValidatorUtils.validateEntity(leaveApplyDTO, AddGroup.class);
        Result result = leaveService.apply(leaveApplyDTO);

        return result;
    }

    @ApiOperation(value = "待办任务", notes = "待办任务")
    @GetMapping("/todo")
//    @RequiresPermissions("sys:act:todo")
    public Result todo(@RequestParam Map<String, Object> params) {
        log.info("待办任务分页查询接口，参数:{}", JSONUtil.toJsonStr(params));

        // 默认管理员方便测试
        String userId = getUserId();
        params.put("userId", userId);
        PageUtils page = leaveService.queryTodoPage(params);

        return Result.ok(page);
    }

    @ApiOperation(value = "已办任务", notes = "已办任务")
    @GetMapping("/done")
//    @RequiresPermissions("sys:act:done")
    public Result done(@RequestParam Map<String, Object> params) {
        log.info("已办任务分页查询接口，参数:{}", JSONUtil.toJsonStr(params));

        // 默认管理员方便测试
        String userId = getUserId();
        params.put("userId", userId);
        PageUtils page = leaveService.queryDonePage(params);

        return Result.ok(page);
    }

    @ApiOperation(value = "运行中的流程", notes = "运行中的流程")
    @GetMapping("/running")
//    @RequiresPermissions("sys:act:running")
    public Result running(@RequestParam Map<String, Object> params) {
        log.info("运行中的流程分页查询接口，参数:{}", JSONUtil.toJsonStr(params));

        // 默认管理员方便测试
        String userId = getUserId();
        params.put("userId", userId);
        PageUtils page = leaveService.queryRunningPage(params);

        return Result.ok(page);
    }

    @ApiOperation(value = "结束的流程", notes = "结束的流程")
    @GetMapping("/finish")
//    @RequiresPermissions("sys:act:finish")
    public Result finish(@RequestParam Map<String, Object> params) {
        log.info("结束的流程分页查询接口，参数:{}", JSONUtil.toJsonStr(params));

        // 默认管理员方便测试
        String userId = getUserId();
        params.put("userId", userId);
        PageUtils page = leaveService.queryFinishPage(params);

        return Result.ok(page);
    }

    @ApiOperation(value = "历史活动节点", notes = "历史活动节点")
    @GetMapping("/history/activity/{taskId}")
    public Result historyActivity(@PathVariable("taskId") String taskId) {
        log.info("历史活动节点查询接口，taskId:{}", taskId);
        List<HistoricActivityInstance> list = leaveService.historyActivity(taskId);

        return Result.ok(list);
    }

    @ApiOperation(value = "历史活动任务", notes = "历史活动任务")
    @GetMapping("/history/task/{id}")
    public Result historyTask(@PathVariable("id") String id) {
        log.info("历史任务查询接口，id:{}", id);
        List<ActHiTaskinst> list = leaveService.historyTaskList(id);

        return Result.ok(list);
    }

    @ApiOperation(value = "签收任务", notes = "签收任务")
    @GetMapping(value = "/task/claim/{taskId}")
    @RequiresPermissions("sys:act:claim")
    public Result claim(@PathVariable("taskId") String taskId) {
        // 默认管理员方便测试
        String userId = getUserId();
        log.info("任务签收, 用户：{}, taskId:{}", userId, taskId);
        try {
            taskService.claim(taskId, userId);
        } catch (Exception e) {
            throw new YYException("签收任务失败taskId：" + taskId, e);
        }

        return Result.ok();
    }

    @ApiOperation(value = "委托任务", notes = "委托任务")
    @GetMapping(value = "/task/entrust")
    @RequiresPermissions("sys:act:entrust")
    public Result entrust(@RequestParam("taskId") String taskId, @RequestParam("userId") String userId) {
        log.info("委托任务, 用户：{}, taskId:{}", userId, taskId);
        // 默认管理员方便测试
        String owner = getUserId();
        // 转办操作
        taskService.setAssignee(taskId, userId);
        // 如果想记录之前输入谁可以设置
        taskService.setOwner(taskId, owner);

        return Result.ok();
    }



    @ApiOperation(value = "完成任务", notes = "完成任务")
    @PostMapping(value = "/task/complete")
    @RequiresPermissions("sys:act:complete")
    public Result complete(@RequestBody LeaveOperateDTO leaveOperate) {
        ValidatorUtils.validateEntity(leaveOperate, AddGroup.class);

        // 默认管理员方便测试
        String userId = getUserId();
        log.info("完成任务, 用户：{}, 参数:{}", userId, JSONUtil.toJsonStr(leaveOperate));
        Result result = leaveService.complete(leaveOperate);

        return result;
    }

    @ApiOperation(value = "读取带跟踪的图片", notes = "读取带跟踪的图片")
    @GetMapping(value = "/process/trace/auto/{id}")
    public void readResource(@PathVariable("id") String id, HttpServletResponse response)
            throws Exception {
        String businessKey = ActivitiConstant.LEAVE_BUSINESSKEY + id;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        InputStream imageStream;
        // 存活
        if (processInstance != null) {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstance.getId());
            // 不使用spring请使用下面的两行代码
            // ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
            // Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

            // 使用spring注入引擎请使用下面的这行代码
            ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
            Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds,
                    Collections.<String>emptyList(), "宋体", "宋体", "宋体", null, 1.0);
        } else {
            HistoricProcessInstance historyProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey)
                    .singleResult();
            imageStream = actReProcdefService.readResource(historyProcessInstance.getProcessDefinitionId(),
                    null, ActivitiConstant.IMAGE);
        }


        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
}
