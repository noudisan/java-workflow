package com.imooc.activiti.activitidemo.coreapi;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Description 历史服务测试
 * @Author 胡浩
 * @Date 2019/8/22
 **/
public class HistoryServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("coreapi/activiti_history.cfg.xml");

    /**
     *
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testHistoryService() {
        HistoryService historyService = activitiRule.getHistoryService();
        //通过构造器启动
        ProcessInstanceBuilder processInstanceBuilder = activitiRule.getRuntimeService().createProcessInstanceBuilder();
        //持久变量
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key0", "value0");
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        //瞬时变量
        Map<String, Object> transientVariables = Maps.newHashMap();
        transientVariables.put("tkey0", "tvalue0");
        transientVariables.put("tkey1", "tvalue1");

        ProcessInstance processInstance = processInstanceBuilder.processDefinitionKey("my-process")
                .processDefinitionKey("my-process")
                .variables(variables)//持久变量
                .transientVariables(transientVariables)//瞬时变量
                .start();
        //修改变量
        activitiRule.getRuntimeService().setVariable(processInstance.getId(), "key1", "value1_1");
        //获取任务
        Task task = activitiRule.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        //提交完成
//        activitiRule.getTaskService().complete(task.getId());

        Map<String, String> properties = Maps.newHashMap();
        properties.put("fKey1", "fValue1");
        properties.put("key1", "value_2_2");
        //使用提交表单的形式提交
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);

        //查询流程实例对象
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().listPage(0, 100);
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            LOGGER.info("historicProcessInstance = {}", ToStringBuilder.reflectionToString(historicProcessInstance, ToStringStyle.JSON_STYLE));
        }

        //查询流程执行节点对象
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("historicActivityInstance = {}", historicActivityInstance);
        }

        //查询流程Task对象
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().listPage(0, 100);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            LOGGER.info("historicTaskInstance = {}", ToStringBuilder.reflectionToString(historicTaskInstance, ToStringStyle.JSON_STYLE));
        }

        //查询流程变量
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("historicVariableInstance = {}", historicVariableInstance);
        }

        //查询流程Details
        List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery().listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            LOGGER.info("historicDetail = {}", historicDetail);
        }

        //流程对象的唯一记录、并把历史记录以日志形式输出
        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService.createProcessInstanceHistoryLogQuery(processInstance.getId())
                .includeVariables().includeFormProperties().includeComments().includeTasks()
                .includeActivities().includeVariableUpdates().singleResult();

        List<HistoricData> historicData = processInstanceHistoryLog.getHistoricData();
        for (HistoricData historicDatum : historicData) {
            LOGGER.info("historicDatum = {}", historicDatum);
        }

        //删除
        historyService.deleteHistoricProcessInstance(processInstance.getId());

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().singleResult();
        LOGGER.info("historicProcessInstance = {}", historicProcessInstance);//应该为null、已经删了

    }
}