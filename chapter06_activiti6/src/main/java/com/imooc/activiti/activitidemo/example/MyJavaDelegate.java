package com.imooc.activiti.activitidemo.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


/**
 * @Description serviceTask  服务task
 * @Author 胡浩
 * @Date 2019/8/26
 **/
public class MyJavaDelegate implements JavaDelegate, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyJavaDelegate.class);

    private Expression name;
    private Expression desc;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        if (name != null) {
            Object value = name.getValue(delegateExecution);
            LOGGER.info("name = {}", value);
        }

        if (desc != null) {
            Object value = desc.getValue(delegateExecution);
            LOGGER.info("name = {}", value);
        }

        LOGGER.info("run My Java Delegate {}", this);
    }
}
