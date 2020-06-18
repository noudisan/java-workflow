package com.dztt.gwork.service;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {

    RepositoryService repositoryService;
    RuntimeService runtimeService;
    HistoryService historyService;
    ProcessEngine processEngine;

    //1.启动流程：
    public ProcessInstance strartFlow(String processKey, Map<String, Object> paras) {
        //启动前必须开启,这样才能取得流程发起人
        String userId = paras.get("AuthenticatedUserId").toString();
        //设置流程实例的发起人是当前用户
        Authentication.setAuthenticatedUserId(userId);
        // 取得业务id
        String busiKey = paras.get("busiKey").toString();
        if (StringUtils.isEmpty(processKey)) {
            // logger.error("错误：流程定义未找到！");
        }
        if (null == paras) {
            paras = new HashMap<>();
        }
        Deployment deployment = repositoryService.createDeploymentQuery().processDefinitionKey(processKey).singleResult();
        if (deployment == null) {
            //logger.error("错误：启动流程实例:" + processKey);
        }

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, busiKey, paras); // 同时设置业务的id到流程实例中的BUSINESS_KEY_
        //logger.info("成功：启动流程实例：" + processInstance.getProcessDefinitionName() + ":" + processInstance.getId());
        Authentication.setAuthenticatedUserId(null); // 这个方法最终使用一个ThreadLocal类型的变量进行存储，也就是与当前的线程绑定，所以流程实例启动完毕之后，需要设置为null，防止多线程的时候出问题。
        return processInstance;
    }

    //2.提前终止流程实例：

    public void deleteProcessInstanceById(String processInstanceId) {
        // ""这个参数本来可以写删除原因
        runtimeService.deleteProcessInstance(processInstanceId, "");
    }

    //3.本人发起的流程实例：
    public List<HistoricProcessInstance> getMyStartProcint(String userId) {
        List<HistoricProcessInstance> list = processEngine.getHistoryService() // 历史相关Service
                .createHistoricProcessInstanceQuery()
                // .finished() 已完成的 unfinish 未完成的，或者不加表示全部
                .startedBy(userId)
                .orderByProcessInstanceStartTime().asc()
                .list();

        return list;
    }

    //3.1 本人参与的流程实例：
    public void getHis(String name, int firstResult, int maxResults) {
        //查询指定用户参与的流程信息 （流程历史  用户参与 ）
        List hpis = historyService
                .createHistoricProcessInstanceQuery().involvedUser(name)
                .orderByProcessInstanceStartTime().desc().listPage(firstResult, maxResults);
    }

    //4.挂起与激活流程实例：

    /**
     * @Description: 挂起流程实例
     **/
    public void handUpProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /**
     * @Description:恢复（唤醒）被挂起的流程实例
     **/
    public void activateProcessInstance(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);

    }

    // 5.生成流程图
    public void image() {

// 将生成图片放到文件夹下
        String deploymentId = "801";
// 获取图片资源的名称
        List<String> list = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
// 定义图片资源的名称
        String resourceName = "";
        if (list != null && list.size() > 0) {
            for (String name : list) {
                if (name.indexOf(".png") >= 0) {
                    resourceName = name;
                }
            }
        }

// 获取图片的输入流
        InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
// 将图片生成到D盘的根目录下
        File file = new File("D:/" + resourceName);
// 将输入流的图片写到d盘下

    }

    //5.判断流程实例在运行中是否存在

    /**
     * @Description: 判断流程实例在运行中存不存在
     * @Date: 2019/5/26 19:24
     * @Param: procInstanceId 流程实例
     * @return: true表示存在, false表示不存在
     **/
    public Boolean isExistProcIntRunning(String procInstanceId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstanceId).singleResult();
        if (pi == null) {
            return false;
        } else {
            return true;
        }
    }


    //6.查询历史实例中流程实例是否存在
    public Boolean isExistProcHistory(String procInstanceId) {
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInstanceId).singleResult();
        if (hpi == null) {
            return false;
        } else {
            return true;
        }
    }


}
