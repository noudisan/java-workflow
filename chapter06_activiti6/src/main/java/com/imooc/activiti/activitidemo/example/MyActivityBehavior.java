package com.imooc.activiti.activitidemo.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author 胡浩
 * @Date 2019/8/26
 **/
public class MyActivityBehavior implements ActivityBehavior {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyActivityBehavior.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("run My Activity Behavior {}", this);
    }
}
