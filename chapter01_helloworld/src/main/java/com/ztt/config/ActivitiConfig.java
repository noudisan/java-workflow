package com.ztt.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 工作流配置
 */
@Component
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    /**
     * 解決工作流生成图片乱码问题
     *
     * @param processEngineConfiguration processEngineConfiguration
     */
    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
    }

}
