package com.it.cloud.activiti.task.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import java.io.Serializable;

/**
 * @author 司马缸砸缸了
 * @description
 * @date 2019/9/28 16:34
 */

public class Mydelegate implements JavaDelegate, Serializable {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("自定义java类");
        System.out.println("ProcessInstanceId: " + execution.getProcessInstanceId());
        System.out.println("EventName: " + execution.getEventName());
        System.out.println("ProcessInstanceBusinessKey: " + execution.getProcessInstanceBusinessKey());
        System.out.println("ProcessDefinitionId: " + execution.getProcessDefinitionId());
        System.out.println("VariableNames: " + execution.getVariableNames());
    }

}
