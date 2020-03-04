package com.it.cloud.activiti.task.delegate;

import org.activiti.engine.runtime.Execution;
import java.io.Serializable;

/**
 * @author 司马缸砸缸了
 * @description
 * @date 2019/10/2 15:46
 */
public class MyBean implements Serializable {

    public void print(Execution exe) {
        System.out.println("执行JavaBean方法，流程ID：" + exe.getId());
    }
}
