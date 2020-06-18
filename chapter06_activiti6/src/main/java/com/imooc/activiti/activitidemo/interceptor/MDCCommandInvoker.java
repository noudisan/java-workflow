package com.imooc.activiti.activitidemo.interceptor;

import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.activiti.engine.logging.LogMDC;

/**
 * @Description MDC拦截器，所有操作都能打印mdc日志
 * @Author 胡浩
 * @Date 2019/8/19
 **/
public class MDCCommandInvoker extends DebugCommandInvoker {
    @Override
    public void executeOperation(Runnable runnable) {
        boolean mdcEnabled = LogMDC.isMDCEnabled();
        LogMDC.setMDCEnabled(true);
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation) runnable;
            if (operation.getExecution() != null) {
                LogMDC.putMDCExecution(operation.getExecution());
            }
        }

        super.executeOperation(runnable);
        LogMDC.clear();
        if (!mdcEnabled) {
            LogMDC.setMDCEnabled(false);
        }
    }
}