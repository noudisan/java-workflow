package com.imooc.activiti.activitidemo.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
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

import java.util.List;
import java.util.Map;

/**
 * @Description 表单服务测试
 * @Author 胡浩
 * @Date 2019/8/22
 **/
public class FormServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     *
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process-form.bpmn20.xml")
    public void testFormService() {
        FormService formService = activitiRule.getFormService();

        ProcessDefinition processDefinition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();

        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        LOGGER.info("startFormKey = {}", startFormKey);

        //开始startform的formData
        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        for (FormProperty formProperty : formProperties) {
            LOGGER.info("formProperty = {}", ToStringBuilder.reflectionToString(formProperty, ToStringStyle.JSON_STYLE));
        }

        //根据form启动流程
        Map<String, String> properties = Maps.newHashMap();
        properties.put("message", "my test message");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), properties);

        //userTask的formData
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        //properties列表
        List<FormProperty> formProperties1 = taskFormData.getFormProperties();
        for (FormProperty formProperty : formProperties1) {
            LOGGER.info("userTaskFormProperty = {}", ToStringBuilder.reflectionToString(formProperty, ToStringStyle.JSON_STYLE));
        }
        Map<String, String> userTaskProperties = Maps.newHashMap();
        userTaskProperties.put("yesORno", "yes");
        formService.submitTaskFormData(task.getId(),userTaskProperties);

        //查询task是否还存在
        Task task1 = activitiRule.getTaskService().createTaskQuery().taskId(task.getId()).singleResult();
        LOGGER.info("task1 = {}", task1);//task1 = null 执行完了

    }
}