package com.imooc.activiti.activitidemo.bpmn;

import com.imooc.activiti.activitidemo.coreapi.TaskServiceTest;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author 胡浩
 * @Date 2019/8/26
 **/
public class UserTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "bpmn/my-process-usertask.bpmn20.xml")
    public void testUserTask() {
        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        LOGGER.info("find by user1 task = {}", task);
        task = taskService.createTaskQuery().taskCandidateUser("user2").singleResult();
        LOGGER.info("find by user2 task = {}", task);
        task = taskService.createTaskQuery().taskCandidateGroup("group1").singleResult();
        LOGGER.info("find by group1 task = {}", task);

        //委托代理人
        taskService.claim(task.getId(), "user2");
//        taskService.setAssignee(task.getId(), "user2");
        LOGGER.info("claim task.id = {} by user2", task.getId());

        //验证其他人员
        Task user1 = taskService.createTaskQuery().taskCandidateOrAssigned("user1").singleResult();
        LOGGER.info("find by user1 task = {}", user1);//应该是null了
        Task user2 = taskService.createTaskQuery().taskCandidateOrAssigned("user1").singleResult();
        LOGGER.info("find by user2 task = {}", user2);//应该是有的了

    }

    /**
     * 通过taskListener配置候选人和委托人
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "bpmn/my-process-usertask2.bpmn20.xml")
    public void testUserTaskListener() {
        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateUser("user1").singleResult();
        LOGGER.info("find by user1 task = {}", task);

        taskService.complete(task.getId());
    }
}
