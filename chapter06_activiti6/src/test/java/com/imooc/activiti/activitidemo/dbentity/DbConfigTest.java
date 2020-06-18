package com.imooc.activiti.activitidemo.dbentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description 数据库配置测试
 * @Author 胡浩
 * @Date 2019/8/23
 **/
public class DbConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbConfigTest.class);

    //测试、改为自己的mysql数据库
//    @Rule
//    public ActivitiRule activitiRule = new ActivitiRule("dbentity/activiti-mysql.cfg.xml");

    /**
     * 历史数据和身份数据都是可配置的
     */
    @Test
    public void testDbConfig() {
        //测试、改为自己的mysql数据库
        ProcessEngine processEngine =  ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("dbentity/activiti-mysql.cfg.xml")
                .buildProcessEngine();

        ManagementService managementService = processEngine.getManagementService();

        //获取所有表以及表对应的数据量
        Map<String, Long> tableCount = managementService.getTableCount();
        //获取所有表名称
        ArrayList<String> tableNames = Lists.newArrayList(tableCount.keySet());
        //排个序
        Collections.sort(tableNames);
        for (String tableName : tableNames) {
            LOGGER.info("table name = {}", tableName);
        }
        LOGGER.info("tableNames.size  = {}", tableNames.size());//应该是28个，去除history应该有16个

//        LOGGER.info("task1 = {}", task1);//task1 = null 执行完了

    }

    /**
     * 删除表
     */
    @Test
    public void tesDropTable() {
        ProcessEngine processEngine =  ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("dbentity/activiti-mysql.cfg.xml")
                .buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();
        Object o = managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                commandContext.getDbSqlSession().dbSchemaDrop();
                LOGGER.info(" 删除表结构");
                return null;
            }
        });
    }
}