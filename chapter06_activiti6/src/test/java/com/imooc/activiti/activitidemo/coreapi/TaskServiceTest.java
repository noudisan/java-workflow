package com.imooc.activiti.activitidemo.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description TaskService测试
 * @Author 胡浩
 * @Date 2019/8/21
 **/
public class TaskServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * TaskService对Task管理与流程控制
     * <p>
     * Task对象的创建、删除（一般不会手工创建，流程流转到usertask时会自动创建）
     * 查询Task，并驱动Task节点完成执行
     * Task相关参数变量（variable）设置
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process-task.bpmn20.xml")
    public void testTaskService() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message!!!");
        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        //获取当前流程任务
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();

        LOGGER.info("task = {} ", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {} ", task.getDescription());//task.description = some Task my test message!!!

        //当前task设置变量
        taskService.setVariable(task.getId(), "key1", "value1");
        taskService.setVariableLocal(task.getId(), "LocalKey1", "LocalValue1");

        //获取变量
        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
        LOGGER.info("taskServiceVariables = {} ", taskServiceVariables);//{key1=value1, LocalKey1=LocalValue1, message=my test message!!!}
        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());
        LOGGER.info("taskServiceVariablesLocal = {} ", taskServiceVariablesLocal);//{LocalKey1=LocalValue1}

        Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());
        LOGGER.info("processVariables = {} ", processVariables);//{key1=value1, message=my test message!!!}

        //流程执行下一个节点
        Map<String, Object> comVariables = Maps.newHashMap();
        comVariables.put("cKey1", "cValue1");
        taskService.complete(task.getId(), comVariables);

        //查询流程是否还存在
        Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        LOGGER.info("task1 = {} ", task1);//task1 = null
    }

    /**
     * TaskService对Task权限信息
     * <p>
     * 候选用户（candidateUser）和候选组（candidateGroup）
     * 指定拥有人（Owner）和办理人（Assignee）
     * 通过claim设置办理人
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process-task.bpmn20.xml")
    public void testTaskServiceUser() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message!!!");
        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        //获取当前流程任务
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();

        LOGGER.info("task = {} ", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {} ", task.getDescription());//task.description = some Task my test message!!!

        //设置流程发起人
        taskService.setOwner(task.getId(), "user1");
        //设置代办人
        //setAssignee强制设置代办人，不管之前有没有代办人
//        taskService.setAssignee(task.getId(), "huhao");

        //获取候选人列表有、但没指定代办人 ==》我是候选人、但是还没指定代办人的 任务
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateUser("huhao")
                .taskUnassigned()
                .listPage(0, 100);

        for (Task task1 : taskList) {
            try {
                //设置代办人
                taskService.claim(task1.getId(), "huhao");
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
            }
        }

        //跟这个用户相关的、指定task与多少用户相关   ===>  （选择下级审批人）
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinksForTask) {
            LOGGER.info("identityLink = {}", identityLink);
        }

        //任务执行
        //查询以huhao为代办人的任务   ===>  代办任务列表
        List<Task> huhaoTaskList = taskService.createTaskQuery().taskAssignee("huhao").listPage(0, 100);
        //根据列表选择的ID的任务来处理任务
//        taskService.createTaskQuery().taskAssignee("huhao").taskId("id");//特定某个task
        for (Task huhaoTask : huhaoTaskList) {
            Map<String, Object> vars = Maps.newHashMap();
            vars.put("ckey1", "cvalue1");
            //执行
            taskService.complete(huhaoTask.getId(), vars);
        }

        huhaoTaskList = taskService.createTaskQuery().taskAssignee("huhao").listPage(0, 100);
        LOGGER.info("是否为空了 {}", CollectionUtils.isEmpty(huhaoTaskList));
    }

    /**
     * TaskService对Task附件信息
     * <p>
     * 任务附件（Attachment）创建于查询
     * 任务评论（Comment）创建与查询
     * 事件记录（Event）创建与查询
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process-task.bpmn20.xml")
    public void testTaskAttachment() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my test message!!!");
        //启动流程
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        //获取当前流程任务
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();

        taskService.setOwner(task.getId(), "user1");
        //设置代办人
        //setAssignee强制设置代办人，不管之前有没有代办人
        taskService.setAssignee(task.getId(), "huhao");

        //新增附件
        taskService.createAttachment("url",task.getId(),task.getProcessInstanceId(),"name",
                "desc","/url/test/png");

        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
        for (Attachment taskAttachment : taskAttachments) {
            LOGGER.info("taskAttachment = {}", ToStringBuilder.reflectionToString(taskAttachment,ToStringStyle.JSON_STYLE));
        }

        //新增评论
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"我评论了一下啊");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"我又评论了一下啊");

        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment taskComment : taskComments) {
            LOGGER.info("taskComment = {}", ToStringBuilder.reflectionToString(taskComment,ToStringStyle.JSON_STYLE));
        }

        //事件记录
        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event event : taskEvents) {
            LOGGER.info("event = {}", ToStringBuilder.reflectionToString(event,ToStringStyle.JSON_STYLE));
        }
    }
}
