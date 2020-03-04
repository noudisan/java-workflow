package com.it.cloud.modules.activiti.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.it.cloud.common.constants.ActivitiConstant;
import com.it.cloud.common.exceptions.YYException;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.common.utils.Query;
import com.it.cloud.modules.activiti.entity.ActReModelEntity;
import com.it.cloud.modules.activiti.service.IActReModelService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author 司马缸砸缸了
 * @date 2019/8/23 16:09
 * @description
 */
@Service
@Slf4j
public class ActReModelServiceImpl implements IActReModelService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        String name = (String) params.get("name");
        int curPage = Integer.parseInt((String) params.get("page"));
        int limit = Integer.parseInt((String) params.get("limit"));

        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();
        if (StringUtils.isNotBlank(key)) {
            modelQuery.modelKey(key);
        }
        if (StringUtils.isNotBlank(name)) {
            modelQuery.modelNameLike("%" + name + "%");
        }
        // 条目
        List<Model> list = modelQuery.listPage((curPage - 1) * limit, limit);
        //分页对象
        Page<Model> page = new Query<Model>(params).getPage();
        page.setTotal(modelQuery.count());
        page.setRecords(list);

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String save(ActReModelEntity actReModel) {
        String description = actReModel.getDescription();
        String key = actReModel.getKey();
        String name = actReModel.getName();
        //版本号
        int revision = 1;

        // 元数据
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);
        // 模型
        Model model = repositoryService.newModel();
        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());

        repositoryService.saveModel(model);
        String id = model.getId();

        // 保存模型编辑器源文件
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace",
                "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        try {
            repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new YYException("保存模型编辑器源文件失败", e);
        }

        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deploy(String id) {
        Model modelData = repositoryService.getModel(id);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        JsonNode editor = null;
        try {
            editor = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        } catch (IOException e) {
            throw new YYException("部署解析失败", e);
        }
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editor);
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

        String processName = modelData.getName();
        if (!StringUtils.endsWith(processName, ActivitiConstant.BPMN20)) {
            processName += ActivitiConstant.BPMN20;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
        Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .addInputStream(processName, in)
                .deploy();
    }

    @Override
    public void export(String id, String type, HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(id);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());
            JsonNode editor = new ObjectMapper().readTree(modelEditorSource);
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editor);
            String mainProcessId = bpmnModel.getMainProcess().getId();

            String filename = "";
            byte[] exportBytes = null;
            if (type.equals("xml")) {
                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                exportBytes = xmlConverter.convertToXML(bpmnModel);
                filename = mainProcessId + ".bpmn20.xml";
            } else if (type.equals("json")) {
                exportBytes = modelEditorSource;
                filename = mainProcessId + ".json";

            }

            ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
            IOUtils.copy(in, response.getOutputStream());
            // filename = URLEncoder.encode(bpmnModel.getMainProcess().getName() + ".bpmn20.xml", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            throw new YYException("导出model的xml文件失败，模型ID：" + id, e);
        }
    }

    @Override
    public void deleteBatch(String[] ids) {
        Optional.ofNullable(ids).ifPresent(item -> {
            Arrays.stream(item).forEach(id -> {
                repositoryService.deleteModel(id);
            });
        });
    }

}


