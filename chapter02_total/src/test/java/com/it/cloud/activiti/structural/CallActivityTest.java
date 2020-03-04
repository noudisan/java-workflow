package com.it.cloud.activiti.structural;

import com.it.cloud.modules.activiti.service.IActReModelService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
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
 * 调用式子流程
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CallActivityTest {

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
                .addClasspathResource("diagrams/subProcess.bpmn20.xml") // 子流程资源文件
                .addClasspathResource("diagrams/callActivity.bpmn20.xml") // 主流程资源文件
                .name("callActivity流程") // 流程名称
                .deploy(); // 部署
        System.out.println("流程部署ID:" + deployment.getId());
        System.out.println("流程部署Name:" + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void start() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("callActivity"); // 流程定义表的KEY字段值
        System.out.println("流程实例ID:" + pi.getId());
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());

        //查询任务
        List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
                .taskAssignee("callUser") // 指定某个人
                .list();
        for (Task task : taskList) {
            System.out.println("--------------------------------------------");
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务创建时间:" + task.getCreateTime());
            System.out.println("任务委派人:" + task.getAssignee());
            System.out.println("流程实例ID:" + task.getProcessInstanceId());

            // 参数
            Map<String, Object> vars = new HashMap<String, Object>();
            vars.put("days", 10);
            // 完成任务
            taskService.complete(task.getId(), vars);
        }

        // 主流程中查询参数
        Integer days = (Integer) runtimeService.getVariable(pi.getId(), "days");
        System.out.println("主流程中参数days:" + days);

        // 调用式子流程中查询参数
        ProcessInstance subPi = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("subProcess")
                .singleResult();
        Integer subDays = (Integer) runtimeService.getVariable(subPi.getId(), "subDays");
        System.out.println("子流程中参数subDays:" + subDays);

        // 设置子流程参数
        runtimeService.setVariable(subPi.getId(), "subDays", subDays -1);

        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 查询任务
        taskList = taskService.createTaskQuery() // 创建任务查询
                .taskAssignee("user10") // 指定某个人
                .list();
        for (Task task : taskList) {
            System.out.println("--------------------------------------------");
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务创建时间:" + task.getCreateTime());
            System.out.println("任务委派人:" + task.getAssignee());
            System.out.println("流程实例ID:" + task.getProcessInstanceId());

            // 完成任务
            taskService.complete(task.getId());
        }


        //查询任务
        taskList = taskService.createTaskQuery() // 创建任务查询
                .taskAssignee("callUser1") // 指定某个人
                .list();
        for (Task task : taskList) {
            System.out.println("--------------------------------------------");
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务创建时间:" + task.getCreateTime());
            System.out.println("任务委派人:" + task.getAssignee());
            System.out.println("流程实例ID:" + task.getProcessInstanceId());
        }

        System.out.println("--------------------------------------------");
        // 主流程中查询参数
        Integer mainDays = (Integer) runtimeService.getVariable(pi.getId(), "mainDays");
        System.out.println("主流程中参数mainDays:" + mainDays);

    }

    /**
     * 查看任务
     */
    @Test
    public void queryTask() {
        List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
                .taskAssignee("admin") // 指定某个人
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
        taskService.complete("270011");
    }

}
