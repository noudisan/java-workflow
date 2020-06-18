package com.imooc.activiti.activitidemo.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 定时任务event
 * @Author 胡浩
 * @Date 2019/8/20
 **/
public class JobEventListener implements ActivitiEventListener {


    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiEventListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        ActivitiEventType eventType = activitiEvent.getType();
        String name = eventType.name();

        if (name.startsWith("TIMER") || name.startsWith("JOB")) {
            LOGGER.info("监听TIMER事件 和 JOB事件 {} \t {}", eventType, activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
