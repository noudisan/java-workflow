package com.it.cloud.activiti.assignuser;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 *个人任务分配监听器
 */
public class UserTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegateTask) {
		// 指定办理人
		delegateTask.setAssignee("zhangsan");
		// 指定候选人
//		delegateTask.addCandidateUser("张三");
//		delegateTask.addCandidateUser("李四");
//		delegateTask.addCandidateUser("王五");
	
	}

}
