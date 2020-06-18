package com.imooc.activiti.activitidemo.coreapi;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description 用户IdentityService测试
 * @Author 胡浩
 * @Date 2019/8/21
 **/
public class IdentityServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     *
     */
    @Test
    public void testIdentityService() {
        IdentityService identityService = activitiRule.getIdentityService();
        List<User> userList1 = identityService.createUserQuery().listPage(0, 100);
        for (User user : userList1) {
            LOGGER.info("user = {}", ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
        }
        LOGGER.info("userList1 = {}", userList1.size());


        User user1 = identityService.newUser("huhao");
        user1.setEmail("huhao@126.com");

        User user2 = identityService.newUser("user2");
        user2.setEmail("user2@126.com");

        identityService.saveUser(user1);
        identityService.saveUser(user2);

        //创建组
        Group group1 = identityService.newGroup("group1");
        identityService.saveGroup(group1);
        Group group2 = identityService.newGroup("group2");
        identityService.saveGroup(group2);

        //用户组与用户关系
        identityService.createMembership(user1.getId(), group1.getId());
        identityService.createMembership(user1.getId(), group2.getId());
        identityService.createMembership(user2.getId(), group1.getId());

        //查询
        List<User> userList = identityService.createUserQuery().memberOfGroup("group1").listPage(0, 100);
        for (User user : userList) {
            LOGGER.info("user = {}", ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
        }

        List<Group> groupList = identityService.createGroupQuery().groupMember("huhao").listPage(0, 100);
        for (Group group : groupList) {
            LOGGER.info("group = {}", ToStringBuilder.reflectionToString(group, ToStringStyle.JSON_STYLE));
        }

        //查所有
        userList1 = identityService.createUserQuery().listPage(0, 100);
        for (User user : userList1) {
            LOGGER.info("user = {}", ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
        }
        LOGGER.info("userList1 = {}", userList1.size());


    }
}