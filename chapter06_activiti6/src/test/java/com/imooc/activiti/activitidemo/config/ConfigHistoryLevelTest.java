package com.imooc.activiti.activitidemo.config;

import com.google.common.collect.Maps;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * @Description HistoryLevel配置
 * @Author 胡浩
 * @Date 2019/8/16
 **/
public class ConfigHistoryLevelTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHistoryLevelTest.class);

    //将启动activiti引擎构建好了
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("config/activiti_history.cfg.xml");

    @Test
    @Deployment(resources = "config/mdc-process.bpmn20.xml")
    public void testConfig() {
        //启动流程
        startProcessInstance();
        //修改变量
        changeVariable();
        //提交表单 task
        submitTaskFormData();

        //输出历史内容
        //输出历史活动
        showHistoryActivity();

        //历史变量
        showHistoryVariable();

        //输出历史表单
        showHistoryTask();

        //历史表单
        List<HistoricDetail> historicDetailsForm = showHistoryForm();

        //输出历史详情
        showHistoryDetails(historicDetailsForm);
    }

    /**
     * 输出历史详情
     *
     * @param historicDetailsForm
     */
    private void showHistoryDetails(List<HistoricDetail> historicDetailsForm) {
        List<HistoricDetail> historicDetails = activitiRule.getHistoryService().createHistoricDetailQuery().listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            LOGGER.info("historicDetail = {}", toString(historicDetail));
        }
        LOGGER.info("historicDetailsForm.size {}", historicDetailsForm.size());
    }

    /**
     * 历史表单
     *
     * @return
     */
    private List<HistoricDetail> showHistoryForm() {
        List<HistoricDetail> historicDetailsForm = activitiRule.getHistoryService().createHistoricDetailQuery().formProperties().listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetailsForm) {
            LOGGER.info("historicDetailsForm = {}", toString(historicDetail));
        }
        LOGGER.info("historicDetailsForm.size {}", historicDetailsForm.size());
        return historicDetailsForm;
    }

    /**
     * 历史任务
     */
    private void showHistoryTask() {
        List<HistoricTaskInstance> historicTaskInstances = activitiRule.getHistoryService().createHistoricTaskInstanceQuery().listPage(0, 100);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            LOGGER.info("historicTaskInstance = {}", historicTaskInstance);
        }
        LOGGER.info("historicTaskInstances.size {}", historicTaskInstances.size());
    }

    /**
     * 历史变量
     */
    private void showHistoryVariable() {
        List<HistoricVariableInstance> historicVariableInstances = activitiRule.getHistoryService()
                .createHistoricVariableInstanceQuery().listPage(0, 100);
        for (HistoricVariableInstance historicActivityInstance : historicVariableInstances) {
            LOGGER.info("historicActivityInstance = {}", historicActivityInstance);
        }
        LOGGER.info("historicVariableInstances.size {}", historicVariableInstances.size());
    }

    /**
     * 输出历史活动
     */
    private void showHistoryActivity() {
        List<HistoricActivityInstance> historicActivityInstances = activitiRule.getHistoryService()
                .createHistoricActivityInstanceQuery().listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("historicActivityInstance = {}", historicActivityInstance);
        }
        LOGGER.info("historicActivityInstances.size {}", historicActivityInstances.size());
    }

    /**
     * 提交表单
     */
    private void submitTaskFormData() {
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Map<String, String> properties = Maps.newHashMap();
        properties.put("formKey1", "valuef1");
        properties.put("formKey2", "valuef2");
        // 提交后、会将一部分内容移到历史数据中
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);
    }

    /**
     * 更改变量
     */
    private void changeVariable() {
        List<Execution> executions = activitiRule.getRuntimeService().createExecutionQuery().listPage(0, 100);
        for (Execution execution : executions) {
            LOGGER.info("execution = {}", execution);
        }
        LOGGER.info("executions.size {}", executions.size());
        String id = executions.iterator().next().getId();
        activitiRule.getRuntimeService().setVariable(id, "keyStart1", "value1_");
    }

    /**
     * 发起流程
     */
    private void startProcessInstance() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("keyStart1", "value1");
        params.put("keyStart2", "value2");
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process", params);
    }

    /**
     * toString
     *
     * @param historicDetail
     * @return
     */
    static String toString(HistoricDetail historicDetail) {
        return ToStringBuilder.reflectionToString(historicDetail,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
