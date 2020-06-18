package com.imooc.activiti.activitidemo.delegage;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Description serviceTask  服务task
 * @Author 胡浩
 * @Date 2019/8/19
 **/
public class MDCErrorServiceDelegate implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(MDCErrorServiceDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) {
        LOGGER.info("run MDCErrorDelegage");
    }
}
