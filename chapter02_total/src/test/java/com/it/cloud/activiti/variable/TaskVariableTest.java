package com.it.cloud.activiti.variable;

import com.it.cloud.activiti.entity.Leave;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务变量测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskVariableTest {

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
                .addClasspathResource("diagrams/helloworld.bpmn20.xml") // 加载流程资源文件
                .name("helloworld流程") // 流程名称
                .deploy(); // 部署
        System.out.println("流程部署ID:" + deployment.getId());
        System.out.println("流程部署Name:" + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void start() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("helloworld"); // 流程定义表的KEY字段值
        System.out.println("流程实例ID:" + pi.getId());
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());
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
        taskService.complete("85005");
    }


    /**
     * 设置任务变量--直接方式
     */
    @Test
    public void setVariable1() {
        String taskId = "10005";
        // 基本类型
        taskService.setVariable(taskId, "days", 5);
        taskService.setVariable(taskId, "reason", "回家结婚");
        // setVariableLocal设置局部变量，只能在当前任务中得到
        taskService.setVariableLocal(taskId, "date", new Date());

        // 序列化对象
        Leave leave = new Leave();
        leave.setDays(5);
        leave.setReason("相亲");
        taskService.setVariable(taskId, "leave", leave);
    }

    /**
     * 获取任务变量--直接方式
     */
    @Test
    public void getVariable1() {
        String taskId = "10005";
        // 获取基本类型
        Integer days = (Integer) taskService.getVariable(taskId, "days");
        Date date = (Date) taskService.getVariableLocal(taskId, "date");
        String reason = (String) taskService.getVariable(taskId, "reason");
        // 获取对象类型
        Leave leave = (Leave) taskService.getVariable(taskId, "leave");

        System.out.println("天数：" + days);
        System.out.println("日期：" + date);
        System.out.println("原因：" + reason);
        System.out.println("对象：" + leave.getDays() + ", " + leave.getReason());
    }

    /**
     * 设置任务变量--MAP方式
     */
    @Test
    public void setVariable2() {
        String taskId = "10005";
        Leave leave = new Leave();
        leave.setDays(5);
        leave.setReason("相亲");
        // 填充map
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("days", 5);
        variables.put("reason", "相亲");
        variables.put("date", new Date());
        variables.put("leave", leave);

        taskService.setVariables(taskId, variables);
    }

    /**
     * 获取任务变量--MAP方式
     */
    @Test
    public void getVariable2() {
        String taskId = "10005";
        // 获取变量
        Map<String, Object> variables = taskService.getVariables(taskId);
        Integer days = (Integer) variables.get("days");
        String reason = (String) variables.get("reason");
        Date date = (Date) variables.get("date");
        Leave leave = (Leave) variables.get("leave");

        System.out.println("天数：" + days);
        System.out.println("日期：" + date);
        System.out.println("原因：" + reason);
        System.out.println("对象：" + leave.getDays() + "," + leave.getReason());
    }

}
