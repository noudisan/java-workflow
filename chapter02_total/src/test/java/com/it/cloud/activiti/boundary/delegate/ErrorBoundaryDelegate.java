package com.it.cloud.activiti.boundary.delegate;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import java.io.Serializable;

/**
 * @author 司马缸砸缸了
 * @description
 * @date 2019/9/28 16:34
 */

public class ErrorBoundaryDelegate implements JavaDelegate, Serializable {

    @Override
    public void execute(DelegateExecution execution) {
        String errorCode = "aaa";
        System.out.println("抛出错误errorCode："+ errorCode);
        throw new BpmnError(errorCode);
    }

}
