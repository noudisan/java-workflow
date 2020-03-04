package com.it.cloud.activiti.listener;

import cn.hutool.json.JSONUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.Map;

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


		String eventName = delegateTask.getEventName();
		// 获取任务变量
		Map<String, Object> variableMap = delegateTask.getVariables();
		System.out.println(JSONUtil.toJsonStr(variableMap));

		if ("create".endsWith(eventName)) {
			System.out.println("create=========");
		} else if ("assignment".endsWith(eventName)) {
			System.out.println("assignment========");
		} else if ("complete".endsWith(eventName)) {
			System.out.println("complete===========");
		} else if ("delete".endsWith(eventName)) {
			System.out.println("delete=============");
		}
	}

}
