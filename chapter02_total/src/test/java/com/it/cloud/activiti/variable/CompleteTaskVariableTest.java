package com.it.cloud.activiti.variable;

import com.it.cloud.activiti.entity.Leave;
import com.it.cloud.modules.activiti.service.IActReModelService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
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
 * 流程变量测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CompleteTaskVariableTest {

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
     * 完成任务2
     */
    @Test
    public void completeTask2() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("var_complete", "var_complete");
        taskService.complete("92505", variables);
    }

    /**
     * 获取流程变量
     */
    @Test
    public void getVariable() {
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                .executionId("92501")
                .variableName("var_complete")
                .list();

        if (list != null && list.size() > 0) {
            for (HistoricVariableInstance hiv : list) {
                System.out.println(hiv.getVariableName() + "=" + hiv.getValue());
            }
        }
    }

}
