package com.imooc.activiti.activitidemo.config;

import org.activiti.engine.runtime.Job;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * @Description 数据库配置测试
 * @Author 胡浩
 * @Date 2019/8/16
 **/
public class ConfigJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigJobTest.class);

    //将启动activiti引擎构建好了
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("config/activiti_job.cfg.xml");

    @Test
    @Deployment(resources = "config/job-process.bpmn20.xml")
    public void testConfig() throws InterruptedException {
        LOGGER.info("start");
        List<Job> jobs = activitiRule.getManagementService().createTimerJobQuery().listPage(0, 100);
        for (Job job : jobs) {
            LOGGER.info("定时任务{}，默认重试次数 = {}", job, job.getRetries());
        }
        LOGGER.info("jobs.size =  {}", jobs.size());
        Thread.sleep(1000 * 100);

        LOGGER.info("end");
    }
}
