package com.imooc.activiti.activitidemo.coreapi;

import com.imooc.activiti.activitidemo.mapper.MyCustomMapper;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.runtime.DeadLetterJobQuery;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.SuspendedJobQuery;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Description ManagementService服务测试
 * @Author 胡浩
 * @Date 2019/8/22
 **/
public class ManagementServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("coreapi/activiti_job.cfg.xml");

    /**
     *
     */
    @Test
    //异步job时，需要在配置文件里配置开启job
    @org.activiti.engine.test.Deployment(resources = "coreapi/job-process.bpmn20.xml")
    public void testJobService() {
        ManagementService managementService = activitiRule.getManagementService();
        //timerJob
        List<Job> timerJobLists = managementService.createTimerJobQuery().listPage(0, 100);
        for (Job timerJob : timerJobLists) {
            LOGGER.info("timerJob= {}", timerJob);
        }
        //普通job
        JobQuery jobQuery = managementService.createJobQuery();

        //中断的job
        SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();

//        查询无法执行的工作
        DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();

    }

    /**
     * 通用表查询（TablePageQuery）
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/job-process.bpmn20.xml")
    public void testTalbePageQuery() {
        ManagementService managementService = activitiRule.getManagementService();

        //查询表名
        String tableName = managementService.getTableName(ProcessDefinitionEntity.class);
        TablePage tablePage = managementService.createTablePageQuery().tableName(tableName).listPage(0, 100);
        LOGGER.info("tablePage = {}", ToStringBuilder.reflectionToString(tablePage, ToStringStyle.JSON_STYLE));

        List<Map<String, Object>> rows = tablePage.getRows();
        for (Map<String, Object> row : rows) {
            LOGGER.info("row = {}", row);
        }

    }

    /**
     * 执行自定义的Sql查询（executeCustomSql）
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testExecuteCustomSql() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        ManagementService managementService = activitiRule.getManagementService();

        List<Map<String, Object>> maps = managementService.executeCustomSql(new AbstractCustomSqlExecution<MyCustomMapper, List<Map<String, Object>>>(MyCustomMapper.class) {
            @Override
            public List<Map<String, Object>> execute(MyCustomMapper myCustomMapper) {
                return myCustomMapper.findAll();
            }
        });

        for (Map<String, Object> map : maps) {
            LOGGER.info("map = {}", map);
        }

    }

    /**
     * 执行command
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testCommand() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");

        ManagementService managementService = activitiRule.getManagementService();

        ProcessDefinitionEntity processDefinitionEntity = managementService.executeCommand(new Command<ProcessDefinitionEntity>() {
            @Override
            public ProcessDefinitionEntity execute(CommandContext commandContext) {
                ProcessDefinitionEntity latestProcessDefinitionByKey = commandContext.getProcessDefinitionEntityManager()
                        .findLatestProcessDefinitionByKey("my-process");
                return latestProcessDefinitionByKey;
            }
        });

        LOGGER.info("processDefinitionEntity = {}", processDefinitionEntity);

    }
}