package com.it.cloud.activiti.task;

import com.it.cloud.modules.activiti.service.IActReModelService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手动和接收任务测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReceiveTaskTest {

    @Autowired
    private IActReModelService actReModelService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 部署流程定义
     */
    @Test
    public void deploy() {
        Deployment deployment = repositoryService.createDeployment() // 创建部署
                .addClasspathResource("diagrams/receiveTask.bpmn20.xml") // 加载流程资源文件
                .name("serviceTask流程") // 流程名称
                .deploy(); // 部署
        System.out.println("流程部署ID:" + deployment.getId());
        System.out.println("流程部署Name:" + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void start() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("receiveTask"); // 流程定义表的KEY字段值
        System.out.println("流程实例ID:" + pi.getId());
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
    }

    /**
     * 查看任务
     */
    @Test
    public void queryTask() {
        List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
                .processInstanceBusinessKey("receiveTask")
                .list();

        System.out.println("任务数量:" + taskList.size());
    }

    /**
     * 查看流程
     */
    @Test
    public void queryExecution() {
        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId("192501")
                .onlyChildExecutions()
                .singleResult();

        System.out.println("执行ID:" + execution.getId());
        System.out.println("执行NAME:" + execution.getName());
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        // 触发
        runtimeService.trigger("192502");
    }

}
