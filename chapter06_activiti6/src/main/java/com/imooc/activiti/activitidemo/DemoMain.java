package com.imooc.activiti.activitidemo;

import com.google.common.collect.Maps;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @Description
 * @Author 胡浩
 * @Date 2019/8/15
 **/
public class DemoMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

    public static void main(String[] args) throws ParseException {
        LOGGER.info("启动程序");
        //创建流程引擎
        ProcessEngine processEngine = getProcessEngine();

        //部署流程定义文件
        ProcessDefinition processDefinition = getProcessDefinition(processEngine);

        //启动运行流程
        ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);

        //处理流程任务
        processTask(processEngine, processInstance);

        LOGGER.info("结束程序");

    }

    /**
     * 处理流程任务
     *
     * @param processEngine
     * @param processInstance
     * @throws ParseException
     */
    private static void processTask(ProcessEngine processEngine, ProcessInstance processInstance) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        while (processInstance != null && !processInstance.isEnded()) {
            TaskService taskService = processEngine.getTaskService();
            List<Task> list = taskService.createTaskQuery().list();
            LOGGER.info("待处理任务[{}]", list.size());
            for (Task task : list) {
                LOGGER.info("待处理任务[{}]", task.getName());
                //获取提交的内容
                Map<String, Object> variables = getStringObjectMap(processEngine, scanner, task);
                //提交任务
                taskService.complete(task.getId(), variables);
                //获取当前流程最新状态
                processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
            }
        }
        scanner.close();
    }

    /**
     * 提取参数变量
     *
     * @param processEngine
     * @param scanner
     * @param task
     * @return
     * @throws ParseException
     */
    private static Map<String, Object> getStringObjectMap(ProcessEngine processEngine, Scanner scanner, Task task) throws ParseException {
        FormService formService = processEngine.getFormService();
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        Map<String, Object> variables = Maps.newHashMap();
        for (FormProperty property : formProperties) {
            String line = null;
            if (StringFormType.class.isInstance(property.getType())) {
                LOGGER.info("请输入 {} ?", property.getName());
                line = scanner.nextLine();
                variables.put(property.getId(), line);
            } else if (DateFormType.class.isInstance(property.getType())) {
                LOGGER.info("请输入{} ? 格式 （yyyy-MM-dd）", property.getName());
                line = scanner.nextLine();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(line);
                variables.put(property.getId(), date);
            } else {
                LOGGER.info("类型暂不支持 {}  ", property.getType());
            }
            LOGGER.info("请输入内容是 [{}] ?", line);
        }
        return variables;
    }

    /**
     * 启动运行流程
     *
     * @param processEngine
     * @param processDefinition
     */
    private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动运行流程 [{}]", processInstance.getProcessDefinitionKey());
        return processInstance;
    }

    /**
     * 部署流程定义文件
     *
     * @param processEngine
     * @return
     */
    private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("MyProcess.bpmn20.xml");
        Deployment deployment = deploymentBuilder.deploy();
        String deploymentId = deployment.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        LOGGER.info("流程定义文件 [{}]，流程ID[{}]", processDefinition.getName(), processDefinition.getId());
        return processDefinition;
    }

    /**
     * 构建引擎
     *
     * @return
     */
    private static ProcessEngine getProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = cfg.buildProcessEngine();
        String name = processEngine.getName();
        String version = ProcessEngine.VERSION;

        LOGGER.info("流程引擎名称[{}]，版本[{}]", name, version);
        return processEngine;
    }
}
