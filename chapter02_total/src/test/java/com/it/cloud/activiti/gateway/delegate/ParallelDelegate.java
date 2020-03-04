package com.it.cloud.activiti.gateway.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ParallelDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		execution.setVariable("flag", false);
		System.out.println("任务执行..." + execution.getCurrentActivityId());
	}

}
