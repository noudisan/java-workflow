package com.imooc.activiti.activitidemo.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description RepostoryService测试
 * @Author 胡浩
 * @Date 2019/8/21
 **/
public class RepostoryServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepostoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("config/activiti_mdc.cfg.xml");

    /**
     * 部署
     */
    @Test
//    @Deployment(resources = "config/mdc-process.bpmn20.xml")
    public void testRepository() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源")
                .addClasspathResource("MyProcess.bpmn20.xml")
                .addClasspathResource("coreapi/my-process.bpmn20.xml");
        //部署流程
        Deployment deploy = deploymentBuilder.deploy();

        LOGGER.info("deploy = {}", deploy);

        //根据id查询部署文件
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
//        Deployment deployment = deploymentQuery .deploymentId(deploy.getId()).singleResult();
        List<Deployment> deploymentList = deploymentQuery.orderByDeploymenTime().asc().listPage(0, 100);
        for (Deployment deployment : deploymentList) {
            LOGGER.info("deployment = {}", deployment);
        }
        LOGGER.info("deploymentList.size = {}", deploymentList.size());


        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
//                .deploymentId(deploy.getId())
                .orderByProcessDefinitionKey().asc()
                .listPage(0, 100);

        for (ProcessDefinition processDefinition : processDefinitions) {
            LOGGER.info("processDefinition = {} ,version = {} ,key = {} ,id = {}",
                    processDefinition, processDefinition.getVersion(),
                    processDefinition.getKey(),
                    processDefinition.getId());
        }
        LOGGER.info("processDefinitions.size = {}", processDefinitions.size());

    }

    /**
     * 暂停、激活
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition.id", processDefinition.getId());
        //挂起
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());
        try {
            LOGGER.info("挂起后、开始启动");
            //服务启动（因为暂停了，所以会有异常
            activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
//      报错提示：  org.activiti.engine.ActivitiException: Cannot start process instance. Process definition 二级审批流程 (id = my-process:1:3) is suspended
            LOGGER.info("挂起后、启动成功");
        } catch (Exception e) {
            LOGGER.info("挂起后、启动失败");
            LOGGER.info(e.getMessage(), e);
        }

        //激活
        repositoryService.activateProcessDefinitionById(processDefinition.getId());
        LOGGER.info("激活后、开始启动");
        //服务启动
        activitiRule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
        LOGGER.info("激活后、启动成功");

    }

    /**
     * 指定流程文件只能通过一个用户或用户组启动
     * 1、建立用户和用户组
     */
    @Test
    @org.activiti.engine.test.Deployment(resources = "coreapi/my-process.bpmn20.xml")
    public void testCandidateStarter() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("processDefinition.id", processDefinition.getId());

        //设置用户及用户组
        repositoryService.addCandidateStarterUser(processDefinition.getId(), "user");
        repositoryService.addCandidateStarterGroup(processDefinition.getId(), "groupM");
        //校验？Activiti不再带校验，只是保存了process与用户和用户组的关系，需要自己代码里进行校验

        List<IdentityLink> identityLinksList = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
        for (IdentityLink identityLink : identityLinksList) {
            LOGGER.info("identityLink = {}  ", identityLink);
        }
        LOGGER.info("identityLinksList.size = {}", identityLinksList.size());

        //刪除关系
        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(), "groupM");

        List<IdentityLink> identityLinksList1 = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
        for (IdentityLink identityLink : identityLinksList1) {
            LOGGER.info("identityLink = {}  ", identityLink);
        }
        LOGGER.info("identityLinksList1.size = {}", identityLinksList1.size());

    }


}
