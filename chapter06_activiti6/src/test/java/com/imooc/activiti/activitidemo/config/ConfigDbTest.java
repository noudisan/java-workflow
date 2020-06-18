package com.imooc.activiti.activitidemo.config;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 数据库配置测试
 * @Author 胡浩
 * @Date 2019/8/16
 **/
public class ConfigDbTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDbTest.class);

    @Test
    public void testConfig() {
//        ProcessEngineConfiguration configuration =
//                ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
            ProcessEngineConfiguration configuration =
                    ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();

        LOGGER.info("configure = {}", configuration);

        ProcessEngine processEngine = configuration.buildProcessEngine();
        LOGGER.info("获取流程引擎 {}", processEngine.getName());
        processEngine.close();
    }

    @Test
    public void testDruidConfig() {
        ProcessEngineConfiguration configuration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti_druid.cfg.xml");

        LOGGER.info("configure = {}", configuration);

        ProcessEngine processEngine = configuration.buildProcessEngine();
        LOGGER.info("获取流程引擎 {}", processEngine.getName());
        processEngine.close();
    }
}
