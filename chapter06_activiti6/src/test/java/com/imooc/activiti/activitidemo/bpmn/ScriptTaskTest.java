package com.imooc.activiti.activitidemo.bpmn;

import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author 胡浩
 * @Date 2019/8/26
 **/
public class ScriptTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "bpmn/my-process-script.bpmn20.xml")
    public void testScriptTask() {
        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().orderByVariableName().asc().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("variable = {}", historicVariableInstance);
        }
        LOGGER.info("variables .size = {}", historicVariableInstances.size());

    }

    /**
     * script juel取返回值
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "bpmn/my-process-script2.bpmn20.xml")
    public void testScriptTask2() {
        //启动流程

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 3);
        variables.put("key2", 4);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().orderByVariableName().asc().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("variable = {}", historicVariableInstance);
        }
        //mySum自动赋值,juel赋为string的值
        //id=5, name=key1, revision=0, type=integer, longValue=3
//        id=6, name=key2, revision=0, type=integer, longValue=4
//        id=10, name=mySum, revision=0, type=string, textValue=7
        LOGGER.info("variables .size = {}", historicVariableInstances.size());
//        variables .size = 3

    }

    /**
     * script javascript取返回值
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "bpmn/my-process-script3.bpmn20.xml")
    public void testScriptTask3() {
        //启动流程

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 3);
        variables.put("key2", 4);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().orderByVariableName().desc().listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("variable = {}", historicVariableInstance);
        }
        //mySum自动赋值,javascript赋为double的值
        //id=5, name=key1, revision=0, type=integer, longValue=3
//        id=6, name=key2, revision=0, type=integer, longValue=4
//       id=10, name=mySum, revision=0, type=double, doubleValue=7.0
        LOGGER.info("variables .size = {}", historicVariableInstances.size());
//        variables .size = 3
    }

    /**
     * script 引擎测试
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "bpmn/my-process-script3.bpmn20.xml")
    public void testScriptEngine() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("juel");

        Object eval = scriptEngine.eval("${1+2}");

        LOGGER.info("value = {}", eval.getClass());
    }
}
