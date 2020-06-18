package com.imooc.activiti.activitidemo.example;

import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author 胡浩
 * @Date 2019/8/26
 **/
public class MyTaskListener implements TaskListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTaskListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        LOGGER.info("eventName = {}", eventName);
        if (StringUtils.equals("create", eventName)) {
            LOGGER.info("config by listener");

            delegateTask.addCandidateUsers(Lists.newArrayList("user1", "user2"));
            delegateTask.addCandidateGroup("group1");

            delegateTask.setAssignee("kermit");
            delegateTask.setVariable("key1", "value1");
            delegateTask.setDueDate(DateTime.now().plusDays(3).toDate());
        }else {
            LOGGER.info("task complete");
        }
    }
}
