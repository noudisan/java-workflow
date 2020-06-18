package com.imooc.activiti.activitidemo.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
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
 * @Description Hi配置测试
 * @Author 胡浩
 * @Date 2019/8/26
 **/
public class DbHistoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbHistoryTest.class);

    //测试、改为自己的mysql数据库
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("dbentity/activiti-mysql.cfg.xml");

    /**
     * 部署
     */
    @Test
    public void testHistory() {
        //会往act_ge_byteArray表插入一条资源记录
        //会往act_re_deployment表插入一条流程部署记录表
        //会往act_re_procdef表插入一条流程定义信息表
        activitiRule.getRepositoryService().createDeployment().name("流程部署")
                .addClasspathResource("dbentity/my-process.bpmn20.xml")
                .deploy();

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        //act_ru_execution流程实例与分支执行信息插入2条（流程启动+用户任务执行）
        //act_ru_task插入1条记录
        Map<String, Object> variables = Maps.newHashMap();
        //act_ru_variable 插入1条变量记录
        variables.put("key0", "value0");
        variables.put("key1", "value1");
        variables.put("key2", "value2");

        ProcessInstance process = runtimeService.startProcessInstanceByKey("my-process", variables);

        //修改变量值
        runtimeService.setVariable(process.getId(), "key1", "vaule1_1");

        //获取task
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(process.getId()).singleResult();
        //设置owner
        taskService.setOwner(task.getId(), "user1");
        //附件
        taskService.createAttachment("url", task.getId(), process.getId()
                , "attachmentName", "desc", "/url/test.png");
        //评论
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note1");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"record note2");

        //form提交(form提交会将提交的表单保存到act_hi_detail表中，通过task提交不会报存进去）
        Map<String, String> properties = Maps.newHashMap();
        properties.put("key1","value2_1");
        properties.put("key3","value3");
        activitiRule.getFormService().submitTaskFormData(task.getId(),properties);

    }


}