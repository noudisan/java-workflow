package com.imooc.activiti.activitidemo.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @Description
 * @Author 胡浩
 * @Date 2019/8/26
 **/
public class MyJavaBean implements Serializable{
    private String name;

    private static final Logger LOGGER = LoggerFactory.getLogger(MyJavaBean.class);

    public void sayHello() {
        LOGGER.info("sayHello");
    }

    public String getName() {
        LOGGER.info("getName = {}", this.name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyJavaBean(String name) {
        this.name = name;
    }

    public MyJavaBean() {
    }
}
