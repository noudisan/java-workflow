package com.it.cloud.activiti.event.delegate;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import java.net.Socket;

public class ErrorStartRunDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		System.out.println("开始检查2222端口");

		try {
			Socket socket = new Socket("127.0.0.1", 2222);
			System.out.println("端口检查完成");
		} catch (Exception e) {
			System.out.println("检查异常");
			throw new BpmnError("error");
		}
	}

}
