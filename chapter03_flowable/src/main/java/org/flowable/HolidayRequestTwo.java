package org.flowable;

import org.flowable.common.engine.api.management.TablePage;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HolidayRequestTwo {

    public static void main(String[] args) {
        /*ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);*/

        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull")
                .setJdbcUsername("root")
                .setJdbcPassword("root")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // ProcessEngine由ProcessEngineConfiguration实例创建。该实例可以配置与调整流程引擎的设置。
        ProcessEngine processEngine = cfg.buildProcessEngine();

        //流程引擎会将XML文件存储在数据库中，这样可以在需要的时候获取它。
        //流程定义转换为内部的、可执行的对象模型，这样使用它就可以启动流程实例。
        RepositoryService repositoryService = processEngine.getRepositoryService();
       /* Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();*/

        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        Deployment deployment = list.get(0);

        //我们现在可以通过API查询验证流程定义已经部署在引擎中（并学习一些API）。
        // 通过RepositoryService创建的ProcessDefinitionQuery对象实现。
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());

        ManagementService manager = processEngine.getManagementService();
        TablePage page = manager.createTablePageQuery().tableName("ACT_GE_PROPERTY").listPage(0, 10);
        List<Map<String, Object>> row = page.getRows();


        //启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", "TOM");
        variables.put("nrOfHolidays", "7");
        variables.put("description", "play");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);

        //查询与完成任务
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }


        System.out.println("Which task would you like to complete?");
        int taskIndex = Integer.valueOf(1);
        Task task = tasks.get(taskIndex - 1);

        System.out.println("current assignee : "+task.getAssignee());
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        System.out.println(processVariables.get("employee") + " wants " +
                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");


        //scanner.nextLine().toLowerCase().equals("y");
        boolean approved = false;
        variables = new HashMap<String, Object>();
        variables.put("approved", approved);
        taskService.complete(task.getId(), variables);


        //只选择一个特定流程实例的活动
        //只选择已完成的活动
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .finished()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            System.out.println(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");
        }
    }

}
