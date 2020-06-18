package com.imooc.activiti.activitidemo.config;

import com.imooc.activiti.activitidemo.event.CustomEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * @Description 事件监听
 * @Author 胡浩
 * @Date 2019/8/16
 **/
public class ConfigEventListenerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigEventListenerTest.class);

    //将启动activiti引擎构建好了
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("config/activiti_eventListener.cfg.xml");

    //代码监听，不在cfg里配置
    @Test
    @Deployment(resources = "config/mdc-process.bpmn20.xml")
    public void testConfig() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());

        //代码监听
        activitiRule.getRuntimeService().addEventListener(new CustomEventListener());

        //手工发送事件
        activitiRule.getRuntimeService().dispatchEvent(new ActivitiEventImpl(ActivitiEventType.CUSTOM));

    }
}
