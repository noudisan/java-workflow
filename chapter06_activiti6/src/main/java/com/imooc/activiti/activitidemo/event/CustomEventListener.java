package com.imooc.activiti.activitidemo.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 自定义event
 * @Author 胡浩
 * @Date 2019/8/20
 **/
public class CustomEventListener implements ActivitiEventListener {


    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiEventListener.class);

    @Override
    public void onEvent(ActivitiEvent activitiEvent) {
        ActivitiEventType eventType = activitiEvent.getType();
        if (ActivitiEventType.CUSTOM.equals(eventType)) {
            LOGGER.info("监听自定义事件 {} \t {}", eventType, activitiEvent.getProcessInstanceId());
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
