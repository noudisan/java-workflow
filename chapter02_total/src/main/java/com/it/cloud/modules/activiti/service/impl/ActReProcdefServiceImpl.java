package com.it.cloud.modules.activiti.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.it.cloud.common.base.Result;
import com.it.cloud.common.constants.ActivitiConstant;
import com.it.cloud.common.exceptions.YYException;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.common.utils.Query;
import com.it.cloud.modules.activiti.entity.ActReProcdefEntity;
import com.it.cloud.modules.activiti.mapper.ActReProcdefMapper;
import com.it.cloud.modules.activiti.service.IActReProcdefService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-08-23
 */
@Slf4j
@Service
public class ActReProcdefServiceImpl extends ServiceImpl<ActReProcdefMapper, ActReProcdefEntity> implements IActReProcdefService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        String name = (String) params.get("name");
        int curPage = Integer.parseInt((String) params.get("page"));
        int limit = Integer.parseInt((String) params.get("limit"));

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionKey()
                .asc();

        if (StringUtils.isNotBlank(name)) {
            processDefinitionQuery.processDefinitionNameLike("%" + name + "%");
        }
        if (StringUtils.isNotBlank(key)) {
            processDefinitionQuery.processDefinitionKey(key);
        }
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage((curPage - 1) * limit, limit);
        /**
         * 转换类型
         */
        List<ActReProcdefEntity> list = processDefinitionList.stream()
                .map(item -> convertObject(item))
                .collect(Collectors.toList());
        Page<ActReProcdefEntity> page = new Query<ActReProcdefEntity>(params).getPage();
        page.setTotal(processDefinitionQuery.count());
        page.setRecords(list);

        return new PageUtils(page);
    }

    @Override
    public InputStream readResource(String id, String proInsId, String type) {
        if (StringUtils.isBlank(id)) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(proInsId)
                    .singleResult();
            id = processInstance.getProcessDefinitionId();
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(id)
                .singleResult();

        String resourceName = "";
        if (ActivitiConstant.IMAGE.equals(type)) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (ActivitiConstant.XML.equals(type)) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);

        return resourceAsStream;
    }

    @Override
    public Result deploy(String exportDir, MultipartFile file) {
        String fileName = file.getOriginalFilename();

        InputStream fileInputStream = null;
        try {
            fileInputStream = file.getInputStream();
        } catch (IOException e) {
            throw new YYException("上传文件部署失败", e);
        }
        Deployment deployment = null;
        String extension = FilenameUtils.getExtension(fileName);
        if (ActivitiConstant.ZIP.equals(extension) || ActivitiConstant.BAR.equals(extension)) {
            ZipInputStream zip = new ZipInputStream(fileInputStream);
            deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
        } else if (ActivitiConstant.PNG.equals(extension)) {
            deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
        } else if (fileName.indexOf(ActivitiConstant.BPMN20) != -1) {
            deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
        } else if (ActivitiConstant.BPMN.equals(extension)) {
            deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
        } else {
            return Result.error("不支持的文件类型：" + extension);
        }

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
        if (CollectionUtil.isEmpty(list)) {
            return Result.error("部署失败，没有流程");
        }
        // 设置流程分类（可选）
        for (ProcessDefinition processDefinition : list) {
            repositoryService.setProcessDefinitionCategory(processDefinition.getId(), processDefinition.getCategory());
            log.info("部署成功，流程ID:{}", processDefinition.getId());
        }

        return Result.ok();
    }

    @Override
    public Model convertToModel(String id) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = null;
        XMLStreamReader xtr = null;
        try {
            in = new InputStreamReader(bpmnStream, "UTF-8");
            xtr = xif.createXMLStreamReader(in);
        } catch (Exception e) {
            throw new YYException("转模型失败", e);
        }

        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        // Model 实体
        Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getName());
        modelData.setCategory(processDefinition.getCategory());
        modelData.setDeploymentId(processDefinition.getDeploymentId());
        modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count() + 1)));
        // 元数据
        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());

        repositoryService.saveModel(modelData);
        try {
            repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new YYException("转模型失败", e);
        }

        return modelData;
    }

    @Override
    public void startProcessInstanceById(String processDefinitionId) {
        runtimeService.startProcessInstanceById(processDefinitionId);
    }

    @Override
    public Result updateState(String id, Integer state) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(id)
                .singleResult();
        if (state == ActivitiConstant.ONE) {
            // 激活
            if (!processDefinition.isSuspended()) {
                return Result.error("流程状态不能激活");
            }
            repositoryService.activateProcessDefinitionById(id, true, DateUtil.date());
            log.info("已激活ID为[" + id + "]的流程定义");
        } else if (state == ActivitiConstant.TWO) {
            // 挂起
            if (processDefinition.isSuspended()) {
                return Result.error("流程状态不能挂起");
            }
            repositoryService.suspendProcessDefinitionById(id, true, DateUtil.date());
            log.info("已挂起ID为[" + id + "]的流程定义");
        }

        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatch(String[] deploymentIds) {
        // false 不带级联的删除
        // 只能删除没有启动的流程，如果流程启动，就会抛出异常
        //
        // true 能级联的删除
        // 能删除启动的流程，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息

        Arrays.stream(deploymentIds).forEach(deploymentId -> {
            repositoryService.deleteDeployment(deploymentId, false);
        });
    }

    /**
     * 转换类型
     *
     * @param processDefinition
     * @return
     */
    private ActReProcdefEntity convertObject(ProcessDefinition processDefinition) {
        ActReProcdefEntity entity = new ActReProcdefEntity();
        String deploymentId = processDefinition.getDeploymentId();
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        entity.setId(processDefinition.getId());
        entity.setKey(processDefinition.getKey());
        entity.setName(processDefinition.getName());
        entity.setDeployTime(deployment == null ? null : DateUtil.date(deployment.getDeploymentTime()).toTimestamp());
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
}
