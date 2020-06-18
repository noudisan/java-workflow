package com.imooc.activiti.activitidemo;

import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author 胡浩
 * @Date 2019/8/16
 **/
public class DemoMainTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMainTest.class);

    @Test
    public void testConfig() {
        //[activiti.cfg.xml] cannot be opened because it does not exist,基于activiti.cfg.xml
        ProcessEngineConfiguration configuration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();

        LOGGER.info("configure = {}", configuration);
    }

    @Test
    public void testConfig2() {
        //[activiti.cfg.xml] cannot be opened because it does not exist,基于activiti.cfg.xml
        ProcessEngineConfiguration configuration =
                ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();

        LOGGER.info("configure = {}", configuration);
    }

}

