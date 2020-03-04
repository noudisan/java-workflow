package com.it.cloud.activiti.boundary;

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
 * 信号边界事件
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SignalBoundaryTest {

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
                .addClasspathResource("diagrams/signalBoundary.bpmn20.xml") // 加载流程资源文件
                .name("signalBoundary流程") // 流程名称
                .deploy(); // 部署
        System.out.println("流程部署ID:" + deployment.getId());
        System.out.println("流程部署Name:" + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void start() throws InterruptedException {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("signalBoundary"); // 流程定义表的KEY字段值
        System.out.println("流程实例ID:" + pi.getId());
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());

        // 睡一会
        Thread.sleep(1000 * 10);

        System.out.println("--------------------------------------------");
        // 查询任务
        List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
                .taskAssignee("signalUser1") // 指定某个人
                .list();
        for (Task task : taskList) {
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务创建时间:" + task.getCreateTime());
            System.out.println("任务委派人:" + task.getAssignee());
            System.out.println("流程实例ID:" + task.getProcessInstanceId());

            // 完成任务
            taskService.complete(task.getId());
        }

        // 查询任务
        List<Task> taskList1 = taskService.createTaskQuery() // 创建任务查询
                .taskAssignee("signalUser2") // 指定某个人
                .list();
        for (Task task : taskList1) {
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务创建时间:" + task.getCreateTime());
            System.out.println("任务委派人:" + task.getAssignee());
            System.out.println("流程实例ID:" + task.getProcessInstanceId());
        }


        // 睡一会
        Thread.sleep(1000 * 10);

        System.out.println("--------------------------------------------");
        // 发送信号
        runtimeService.signalEventReceived("changeSignal1");

        // 睡一会
        Thread.sleep(1000 * 10);

        // 查询任务
        List<Task> taskList2 = taskService.createTaskQuery() // 创建任务查询
                .taskAssignee("signalUser3") // 指定某个人
                .list();
        for (Task task : taskList2) {
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务创建时间:" + task.getCreateTime());
            System.out.println("任务委派人:" + task.getAssignee());
            System.out.println("流程实例ID:" + task.getProcessInstanceId());
        }
    }

    /**
     * 查看任务
     */
    @Test
    public void queryTask() {
        List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
                .taskAssignee("signalUser3") // 指定某个人
                .list();
        for (Task task : taskList) {
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务创建时间:" + task.getCreateTime());
            System.out.println("任务委派人:" + task.getAssignee());
            System.out.println("流程实例ID:" + task.getProcessInstanceId());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        taskService.complete("275005");
    }

    /**
     * 发送信号
     */
    @Test
    public void eventReceived() {
        // 可以查询所有订阅了特定信号事件的执行流
        List<Execution> executions = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("changeSignal1")
                .list();
        // 把信号发送给全局所有订阅的处理器（广播语义）
        runtimeService.signalEventReceived("changeSignal1");

        // 只把信息发送给指定流程的执行
        runtimeService.signalEventReceived("changeSignal1", executions.get(0).getId());
    }

}
