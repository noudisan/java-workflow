package com.it.cloud.activiti.intermediate;

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
 * 信号中间Catching事件
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntermediateSignCatchTest {

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
                .addClasspathResource("diagrams/intermediateSignCatch.bpmn20.xml") // 加载流程资源文件
                .name("intermediateSignCatch流程") // 流程名称
                .deploy(); // 部署
        System.out.println("流程部署ID:" + deployment.getId());
        System.out.println("流程部署Name:" + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void start() throws InterruptedException {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("intermediateSignCatch"); // 流程定义表的KEY字段值
        System.out.println("流程实例ID:" + pi.getId());
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
        System.out.println("-------------------完美的分割线-------------------");

        // 查询任务
        Task task = taskService.createTaskQuery()
                .taskAssignee("payUser") // 指定某个人
                .singleResult();
        System.out.println("task name: " + task.getName());
        taskService.complete(task.getId());

        // 查询当前流程实例中订阅了“pay”信号的执行流
        List<Execution> executions = runtimeService.createExecutionQuery()
                .processInstanceId(pi.getId())
                .signalEventSubscriptionName("pay")
                .list();
        for (Execution exe: executions) {
            System.out.println("执行流ID： " + exe);
            // 发送信号
            runtimeService.signalEventReceived("pay", exe.getId());
        }

        // 查询任务
        task = taskService.createTaskQuery()
                .taskAssignee("payUser1") // 指定某个人
                .singleResult();
        System.out.println("task name: " + task.getName());
        taskService.complete(task.getId());
    }



}
