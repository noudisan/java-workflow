package com.it.cloud.activiti.listener;

import cn.hutool.json.JSONUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 司马缸砸缸了
 * @date 2019/9/16 10:47
 * @description 监听器
 */
@Component
public class UserExecutionListener implements Serializable, ExecutionListener {

    // ExecutionListener类的实现
    public void notify(DelegateExecution execution) {
        String eventName = execution.getEventName();
        //start
        if ("start".equals(eventName)) {
            System.out.println("start=========");
        } else if ("end".equals(eventName)) {
            System.out.println("end=========");
        } else if ("take".equals(eventName)) {
            //做一些业务处理，例如请假单拒绝后修改业务状态
            System.out.println("take=========BusinessKey:"+ execution.getProcessInstanceBusinessKey());
        }
    }
}
