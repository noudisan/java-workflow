package com.imooc.activiti.activitidemo.bpmn;

import com.google.common.collect.Maps;
import com.imooc.activiti.activitidemo.example.MyJavaBean;
import com.imooc.activiti.activitidemo.example.MyJavaDelegate;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @Description ServiceTaskSpringTest测试
 * @Author 胡浩
 * @Date 2019/8/26
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:bpmn/activiti-context.xml"})
public class ServiceTaskSpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskSpringTest.class);

    @Rule
    @Autowired
    //自动注入进来、取activiti-context里面的配置
    public ActivitiRule activitiRule;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    /**
     * 表达式执行myJavaDelegate
     */
    @Test
    @Deployment(resources = "bpmn/my-process-servicetask4.bpmn20.xml")
    public void testServiceTask() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        Task task = taskService.createTaskQuery().singleResult();
        taskService.complete(task.getId());
    }

    /**
     * 将myJavaDelegate作为对象传进去
     */
    @Test
    @Deployment(resources = "bpmn/my-process-servicetask4.bpmn20.xml")
    public void testServiceTask2() {
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaDelegate myJavaDelegate = new MyJavaDelegate();
        variables.put("myJavaDelegate",myJavaDelegate);
        LOGGER.info("myJavaDelegate = {}",myJavaDelegate);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process",variables);
        Task task = taskService.createTaskQuery().singleResult();
        taskService.complete(task.getId());
    }

    /**
     * 调用方法表达式和值表达式
     */
    @Test
    @Deployment(resources = "bpmn/my-process-servicetask5.bpmn20.xml")
    public void testServiceTask3() {
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaBean myJavaBean = new MyJavaBean("testname");
        variables.put("myJavaBean",myJavaBean);
        LOGGER.info("myJavaBean = {}",myJavaBean);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process",variables);
        Task task = taskService.createTaskQuery().singleResult();
        taskService.complete(task.getId());
    }

}
