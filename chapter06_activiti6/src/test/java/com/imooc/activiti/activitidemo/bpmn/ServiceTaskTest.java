package com.imooc.activiti.activitidemo.bpmn;

import com.google.common.collect.Maps;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author 胡浩
 * @Date 2019/8/26
 **/
public class ServiceTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * JavaDelegate
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "bpmn/my-process-servicetask.bpmn20.xml")
    public void testJavaDelegate() {
        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info(" historicActivityInstance = {}", historicActivityInstance);
        }
//        HistoricActivityInstanceEntity[id=6, activityId=startEvent, activityName=开始] Pro
//        HistoricActivityInstanceEntity[id=7, activityId=someTask, activityName=主管审批] Pro
//        HistoricActivityInstanceEntity[id=8, activityId=endEventCancel, activityName=取消]
//        执行完成了
    }

    /**
     * ActivityBehavior测试
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "bpmn/my-process-servicetask2.bpmn20.xml")
    public void testActivityBehavior() {
        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info(" historicActivityInstance = {}", historicActivityInstance);
        }
//        id=6, activityId=startEvent, activityName=开始]
//        id=7, activityId=someTask, activityName=主管审批]
        //停在了someTask上

        Execution execution = activitiRule.getRuntimeService().createExecutionQuery().activityId("someTask").singleResult();
        LOGGER.info(" execution = {}", execution);
        ManagementService managementService = activitiRule.getManagementService();
        //继续流转
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                ActivitiEngineAgenda agenda = commandContext.getAgenda();
                agenda.planTakeOutgoingSequenceFlowsOperation((ExecutionEntity) execution, false);
                return null;
            }
        });

        historicActivityInstances = activitiRule.getHistoryService().createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info(" historicActivityInstance = {}", historicActivityInstance);
        }

    }

    /**
     * JavaDelegate传参
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "bpmn/my-process-servicetask3.bpmn20.xml")
    public void testJavaDelegateByVariable() {
        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("desc","the test java delegate");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process",variables);

    }

}
