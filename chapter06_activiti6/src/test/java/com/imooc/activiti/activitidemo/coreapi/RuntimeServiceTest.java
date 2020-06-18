package com.imooc.activiti.activitidemo.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Description RepostoryService测试
 * @Author 胡浩
 * @Date 2019/8/21
 **/
public class RuntimeServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("config/activiti_mdc.cfg.xml");

    /**
     * 各种方式对流程启动的控制
     * 通过key启动
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testStartProcessByKey() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        //参数
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        //每次流程部署、id会发生变化，key不会变化
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = {}", processInstance);
    }

    /**
     * 各种方式对流程启动的控制
     * 通过Id启动
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testStartProcessById() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessDefinition processDefinition = activitiRule.getRepositoryService()
                .createProcessDefinitionQuery().singleResult();
        //参数
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        //每次流程部署、id会发生变化，key不会变化
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);
        LOGGER.info("processInstance = {}", processInstance);
    }

    /**
     * 各种方式对流程启动的控制
     * 通过ProcessInstanceBuilder启动
     * 通过构造器启动
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testStartProcessByProcessInstanceBuilder() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        //参数
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");

        //通过构造器启动
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder.businessKey("businessKey001")
                .processDefinitionKey("my-process")
                .variables(variables)
                .start();

        LOGGER.info("processInstance = {}", processInstance);
    }

    /**
     * 各种方式对流程启动的控制
     * 通过message启动流程
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process-message.bpmn20.xml")
    public void testProcessByMessage() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("my-message");
        LOGGER.info("processInstance = {} ", processInstance);
    }

    /**
     * 查询、修改变量
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testVariables() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        //参数
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        variables.put("key3", "value3");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("启动成功processInstance = {}", processInstance);
        //新增参数
        runtimeService.setVariable(processInstance.getId(), "key4", "value4");
        //修改老参数
        runtimeService.setVariable(processInstance.getId(), "key2", "value2_1");

        //查询变量
        Map<String, Object> variables1 = runtimeService.getVariables(processInstance.getId());
        LOGGER.info("variables1 = {}", variables1);//{key1=value1, key2=value2_1, key3=value3, key4=value4}
    }

    /**
     * 流程实例进行查询
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testProcessInstanceQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        //参数
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("启动成功processInstance = {}", processInstance);

        //根据流程实例id查询
        ProcessInstance processInstanceById = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        //根据业务唯一标识查询
        ProcessInstance processInstanceByBusinessKey = runtimeService.createProcessInstanceQuery().processDefinitionKey("businessKey001").singleResult();

    }

    /**
     * 流程对象进行查询
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testExecutionQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        //参数
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("启动成功processInstance = {}", processInstance);

        List<Execution> executions = runtimeService.createExecutionQuery().listPage(0, 100);
        //会查出来2条、一条是流程实例ProcessInstance，一条是Execution
        for (Execution execution : executions) {
            LOGGER.info("execution = {} ", execution);
        }

    }


    /**
     * RuntimeService流程触发
     * 使用trigger触发ReceiveTask节点
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process-trigger.bpmn20.xml")
    public void testTrigger() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        //查询到tlApprove的执行流
        Execution tlApprove = runtimeService.createExecutionQuery().activityId("tlApprove").singleResult();
        LOGGER.info("执行前tlApprove = {} ", tlApprove);

        //执行
        runtimeService.trigger(tlApprove.getId());

        //查询到tlApprove的执行流
        Execution tlApprove1 = runtimeService.createExecutionQuery().activityId("tlApprove").singleResult();
        LOGGER.info("执行后tlApprove1 = {} ", tlApprove1);//执行后tlApprove1 = null,流程已經执行完了
    }

    /**
     * RuntimeService流程触发
     * 触发信号捕获事件signalEventReceived
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process-signal-received.bpmn20.xml")
    public void testSignalEventReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        //查询到信号my-signal
        Execution execution = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        LOGGER.info("execution = {} ", execution);

        //触发信号
        runtimeService.signalEventReceived("my-signal");

        //再查询到信号my-signal
        Execution execution1 = runtimeService.createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        LOGGER.info("执行后execution = {} ", execution1); //执行后execution = null,流程已經执行完了
    }

    /**
     * RuntimeService流程触发
     * 触发消息捕获事件messageEventReceived（必须指定流程的Id）
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process-message-received.bpmn20.xml")
    public void testMessageEventReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        //查询到my-message
        Execution execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();
        LOGGER.info("execution = {} ", execution);

        //触发信号
        runtimeService.messageEventReceived("my-message", processInstance.getId());

        //再查询到my-message
        Execution execution1 = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();
        LOGGER.info("执行后execution = {} ", execution1); //执行后execution = null,流程已經执行完了
    }


}
