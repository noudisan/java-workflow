package com.imooc.activiti.activitidemo.config;

import org.activiti.engine.logging.LogMDC;
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
 * @Description 拦截器配置测试
 * @Author 胡浩
 * @Date 2019/8/16
 **/
public class ConfigInterceptorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigInterceptorTest.class);

    //将启动activiti引擎构建好了
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("config/activiti_interceptor.cfg.xml");

    //命令拦截器
    @Test
    @Deployment(resources = "config/mdc-process.bpmn20.xml")
    public void testConfig() {
        LogMDC.setMDCEnabled(true);
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();

        activitiRule.getTaskService().complete(task.getId());
    }
}
