package com.imooc.activiti.activitidemo.dbentity;

import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description GE通用数据库配置测试
 * @Author 胡浩
 * @Date 2019/8/23
 **/
public class DbRepositoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbRepositoryTest.class);

    //测试、改为自己的mysql数据库
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("dbentity/activiti-mysql.cfg.xml");

    /**
     * 部署
     */
    @Test
    public void testDeploy() {
        //会往act_ge_byteArray表插入一条资源记录
        //会往act_re_deployment表插入一条流程部署记录表
        //会往act_re_procdef表插入一条流程定义信息表
        //<startEvent id="startEvent" name="开始" activiti:formKey="secend_approve_formkey"></startEvent>
        activitiRule.getRepositoryService().createDeployment().name("二次审批流程")
                .addClasspathResource("MyProcess.bpmn20.xml")
                .deploy();
    }

    /**
     * 挂起
     */
    @Test
    public void testSuspend() {
         //挂起
        activitiRule.getRepositoryService().suspendProcessDefinitionById("process:2:7504");

        boolean processDefinitionSuspended = activitiRule.getRepositoryService().isProcessDefinitionSuspended("process:2:7504");
        LOGGER.info("是否挂起 {}",processDefinitionSuspended);
    }


}