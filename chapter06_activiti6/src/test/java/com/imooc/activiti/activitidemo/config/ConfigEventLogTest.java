package com.imooc.activiti.activitidemo.config;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
 * @Description 事件日志
 * @Author 胡浩
 * @Date 2019/8/16
 **/
public class ConfigEventLogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigEventLogTest.class);

    //将启动activiti引擎构建好了
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_event.cfg.xml");

    @Test
    @Deployment(resources = "config/mdc-process.bpmn20.xml")
    public void testConfig() {
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());

        List<EventLogEntry> eventLogEntries = activitiRule.getManagementService().getEventLogEntriesByProcessInstanceId(processInstance.getId());
        for (EventLogEntry eventLogEntry : eventLogEntries) {
            LOGGER.info("eventLog ={} ,eventLog.data = {}", eventLogEntry.getType(), eventLogEntry.toString());
        }
        LOGGER.info("eventLogEntries.size ={} ", eventLogEntries.size());
    }
}
