package com.imooc.activiti.activitidemo.config;

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
 * @Description 数据库配置测试
 * @Author 胡浩
 * @Date 2019/8/16
 **/
public class ConfigMDCTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigMDCTest.class);

    //将启动activiti引擎构建好了
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("config/activiti_mdc.cfg.xml");

    @Test
    @Deployment(resources = "config/mdc-process.bpmn20.xml")
    public void testConfig() {
//        LogMDC.setMDCEnabled(true);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        assertNotNull(processInstance);

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        assertEquals("主管审批", task.getName());
        activitiRule.getTaskService().complete(task.getId());
    }
}
