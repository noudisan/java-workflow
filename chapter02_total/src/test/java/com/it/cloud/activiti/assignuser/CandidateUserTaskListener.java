package com.it.cloud.activiti.assignuser;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 分配候选人监听器
 */
public class CandidateUserTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	public void notify(DelegateTask delegateTask) {
		// 候选人
		delegateTask.addCandidateUser("sima1");
		delegateTask.addCandidateUser("sima2");
		delegateTask.addCandidateUser("sima3");
	
	}

}
