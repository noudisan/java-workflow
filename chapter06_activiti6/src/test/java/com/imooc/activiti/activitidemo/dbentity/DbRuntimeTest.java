package com.imooc.activiti.activitidemo.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Description GE通用数据库配置测试
 * @Author 胡浩
 * @Date 2019/8/23
 **/
public class DbRuntimeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbRuntimeTest.class);

    //测试、改为自己的mysql数据库
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("dbentity/activiti-mysql.cfg.xml");

    /**
     * 部署
     */
    @Test
    public void testRuntime() {
        //会往act_ge_byteArray表插入一条资源记录
        //会往act_re_deployment表插入一条流程部署记录表
        //会往act_re_procdef表插入一条流程定义信息表
        //<startEvent id="startEvent" name="开始" activiti:formKey="secend_approve_formkey"></startEvent>
        activitiRule.getRepositoryService().createDeployment().name("二次审批流程")
                .addClasspathResource("MyProcess.bpmn20.xml")
                .deploy();

        //act_ru_execution流程实例与分支执行信息插入2条（流程启动+用户任务执行）
        //act_ru_task插入1条记录
        Map<String, Object> variables = Maps.newHashMap();
        //act_ru_variable 插入1条变量记录
        variables.put("key1", "value1");
        ProcessInstance process = activitiRule.getRuntimeService().startProcessInstanceByKey("process", variables);
    }

    /**
     * 设置所属人
     */
    @Test
    public void testSetOwner() {
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("process").singleResult();
        //ACT_RU_IDENTITYLINK 插入1条用户记录，并更新act_ru_task的owner字段
        taskService.setOwner(task.getId(), "user1");

    }

    /**
     * 数据库表流程key需要保持唯一、所以为了测试，先把数据库清空
     */
    @Test
    public void testMessage(){
        //会往act_ge_byteArray表插入一条资源记录
        //会往act_re_deployment表插入一条流程部署记录表
        //会往act_re_procdef表插入一条流程定义信息表

        //因为有message，会往act_ru_event_subscr表插入一条记录
//        message形式是、只要接受到这个message信息，就会启动流程
        activitiRule.getRepositoryService().createDeployment().name("测试部署Message")
                .addClasspathResource("coreapi/my-process-message.bpmn20.xml")
                .deploy();

//        //message-received是在usertask上，需要流程实例启动以后，如果接收到该信号，则运行usertask
//        activitiRule.getRepositoryService().createDeployment().name("测试部署MessageReceived")
//                .addClasspathResource("coreapi/my-process-message-received.bpmn20.xml")
//                .deploy();
    }

    /**
     * 数据库表流程key需要保持唯一、所以为了测试，先把数据库清空
     */
    @Test
    public void testMessageReceived(){
        //会往act_ge_byteArray表插入一条资源记录
        //会往act_re_deployment表插入一条流程部署记录表
        //会往act_re_procdef表插入一条流程定义信息表


        //message-received是在usertask上，需要流程实例启动以后，如果接收到该信号，则运行usertask
        activitiRule.getRepositoryService().createDeployment().name("测试部署MessageReceived")
                .addClasspathResource("coreapi/my-process-message-received.bpmn20.xml")
                .deploy();

        //启动
        //插入act_ru_event_subscr表插入一条记录
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

         //        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();


    }


}