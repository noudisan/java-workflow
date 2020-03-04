package com.it.cloud.activiti.task.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import java.io.Serializable;

/**
 * @author 司马缸砸缸了
 * @description
 * @date 2019/9/28 16:34
 */

public class ReceiveTaskDelegate implements JavaDelegate, Serializable {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("ReceiveTaskDelegate  运行结束");
    }

}
