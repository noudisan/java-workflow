package com.imooc.activiti.activitidemo.dbentity;

import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
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
public class DbGeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbGeTest.class);

    //测试、改为自己的mysql数据库
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("dbentity/activiti-mysql.cfg.xml");

    /**
     * 部署记录
     */
    @Test
    public void testByeArray() {
        //会往act_ge_byteArray表插入一条部署记录
        activitiRule.getRepositoryService().createDeployment().name("测试部署")
                .addClasspathResource("dbentity/my-process.bpmn20.xml")
                .deploy();
    }

    /**
     * 手工添加部署记录
     */
    @Test
    public void Insert() {
        //会往act_ge_byteArray表插入一条部署记录
        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                ByteArrayEntity entity = new ByteArrayEntityImpl();
                entity.setName("test");
                entity.setBytes("test message".getBytes());
                commandContext.getByteArrayEntityManager().insert(entity);
                return null;
            }
        });
    }

}