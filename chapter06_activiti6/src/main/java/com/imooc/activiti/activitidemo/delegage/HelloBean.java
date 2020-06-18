package com.imooc.activiti.activitidemo.delegage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author 胡浩
 * @Date 2019/8/20
 **/
public class HelloBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloBean.class);

    public void sayHello() {
        LOGGER.info("== sayHello ==");
    }
}
