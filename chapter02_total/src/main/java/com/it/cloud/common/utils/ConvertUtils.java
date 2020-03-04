package com.it.cloud.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.it.cloud.common.constants.ActivitiConstant;
import com.it.cloud.modules.activiti.entity.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 司马缸砸缸了
 * @date 2019/9/6 18:55
 * @description 类型转换
 */
public class ConvertUtils {

    /**
     * 转换类型ActReProcdefEntity
     *
     * @param processDefinition
     * @return
     */
    public static ActReProcdefEntity convertObject(ProcessDefinition processDefinition) {
        ActReProcdefEntity entity = new ActReProcdefEntity();
        entity.setId(processDefinition.getId());
        entity.setKey(processDefinition.getKey());
        entity.setName(processDefinition.getName());
        entity.setDeploymentId(processDefinition.getDeploymentId());
        entity.setSuspensionState(processDefinition.isSuspended() ? ActivitiConstant.TWO : ActivitiConstant.ONE);
        entity.setResourceName(processDefinition.getResourceName());
        entity.setDgrmResourceName(processDefinition.getDiagramResourceName());
        entity.setCategory(processDefinition.getCategory());
        entity.setVersion(processDefinition.getVersion());
        entity.setDescription(processDefinition.getDescription());
        entity.setEngineVersion(processDefinition.getEngineVersion());
        entity.setTenantId(processDefinition.getTenantId());

        return entity;
    }

    /**
     * 转换类型ActRuTaskEntity
     *
     * @param task
     * @return
     */
    public static ActRuTaskEntity convertObject(Task task) {
        ActRuTaskEntity entity = new ActRuTaskEntity();
        entity.setId(task.getId());
        entity.setName(task.getName());
        entity.setAssignee(task.getAssignee());
        entity.setSuspensionState(task.isSuspended() ? ActivitiConstant.TWO : ActivitiConstant.ONE);
        entity.setOwner(task.getOwner());
        entity.setProcInstId(task.getProcessInstanceId());
        entity.setCategory(task.getCategory());
        entity.setProcDefId(task.getProcessDefinitionId());
        entity.setDescription(task.getDescription());
        entity.setSuspensionState(task.isSuspended() ? ActivitiConstant.TWO : ActivitiConstant.ONE);
        entity.setCreateTime(task.getCreateTime() == null ? null : DateUtil.date(task.getCreateTime()).toTimestamp());
        return entity;
    }

    /**
     * 转换类型List<ActRuTaskEntity>
     *
     * @param taskList
     * @return
     */
    public static List<ActRuTaskEntity> convertActRuTaskEntityList(List<Task> taskList) {
        if (CollectionUtil.isEmpty(taskList)) {
            return null;
        }
        List<ActRuTaskEntity> list = taskList.stream()
                .map(item -> {
                    return convertObject(item);
                })
                .collect(Collectors.toList());

        return list;
    }

    /**
     * 转换类型ActHiTaskinst
     *
     * @param task
     * @return
     */
    public static ActHiTaskinst convertObject(HistoricTaskInstance task) {
        ActHiTaskinst entity = new ActHiTaskinst();
        entity.setId(task.getId());
        entity.setName(task.getName());
        entity.setAssignee(task.getAssignee());
        entity.setDuration(task.getDurationInMillis());
        entity.setOwner(task.getOwner());
        entity.setProcInstId(task.getProcessInstanceId());
        entity.setCategory(task.getCategory());
        entity.setProcDefId(task.getProcessDefinitionId());
        entity.setDescription(task.getDescription());
        entity.setStartTime(task.getStartTime() == null ? null : DateUtil.date(task.getStartTime()).toTimestamp());
        entity.setEndTime(task.getEndTime() == null ? null : DateUtil.date(task.getEndTime()).toTimestamp());
        return entity;
    }


    /**
     * 转换类型List<ActReProcdefEntity>
     *
     * @param sourceList
     * @return
     */
    public static List<ActHiTaskinst> convertActHiTaskinstList(List<HistoricTaskInstance> sourceList) {
        if (CollectionUtil.isEmpty(sourceList)) {
            return null;
        }
        List<ActHiTaskinst> list = sourceList.stream()
                .map(item -> {
                    return convertObject(item);
                })
                .collect(Collectors.toList());

        return list;
    }

    /**
     * 转换类型ActHiTaskinst
     *
     * @param task
     * @return
     */
    public static ActRuExecution convertObject(ProcessInstance instance) {
        ActRuExecution entity = new ActRuExecution();
        entity.setId(instance.getId());
        entity.setName(instance.getName());
        entity.setBusinessKey(instance.getBusinessKey());
        entity.setSuspensionState(instance.isSuspended() ? ActivitiConstant.TWO : ActivitiConstant.ONE);
        // 设置流程定义name
        entity.setName(instance.getProcessDefinitionName());
        entity.setProcDefId(instance.getProcessDefinitionId());
        entity.setStartTime(instance.getStartTime() == null ? null : DateUtil.date(instance.getStartTime()).toTimestamp());

        return entity;
    }


    /**
     * 转换类型List<ActRuExecution>
     *
     * @param sourceList
     * @return
     */
    public static List<ActRuExecution> convertActRuExecutionList(List<ProcessInstance> sourceList) {
        if (CollectionUtil.isEmpty(sourceList)) {
            return null;
        }
        List<ActRuExecution> list = sourceList.stream()
                .map(item -> {
                    return convertObject(item);
                })
                .collect(Collectors.toList());

        return list;
    }

    /**
     * 转换类型ActHiProcinst
     *
     * @param task
     * @return
     */
    public static ActHiProcinst convertObject(HistoricProcessInstance instance) {
        ActHiProcinst entity = new ActHiProcinst();
        entity.setId(instance.getId());
        entity.setName(instance.getName());
        entity.setBusinessKey(instance.getBusinessKey());
        // 设置流程定义name
        entity.setName(instance.getProcessDefinitionName());
        entity.setProcDefId(instance.getProcessDefinitionId());
        entity.setStartTime(instance.getStartTime() == null ? null : DateUtil.date(instance.getStartTime()).toTimestamp());
        entity.setEndTime(instance.getEndTime() == null ? null : DateUtil.date(instance.getEndTime()).toTimestamp());
        entity.setDuration(instance.getDurationInMillis());

        return entity;
    }


    /**
     * 转换类型List<ActRuExecution>
     *
     * @param sourceList
     * @return
     */
    public static List<ActHiProcinst> convertActHiProcinstList(List<HistoricProcessInstance> sourceList) {
        if (CollectionUtil.isEmpty(sourceList)) {
            return null;
        }
        List<ActHiProcinst> list = sourceList.stream()
                .map(item -> {
                    return convertObject(item);
                })
                .collect(Collectors.toList());

        return list;
    }
}
