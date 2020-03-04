package com.it.cloud.modules.activiti.service;

import com.it.cloud.common.base.Result;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.modules.activiti.entity.ActReProcdefEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.activiti.engine.repository.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-08-23
 */
public interface IActReProcdefService extends IService<ActReProcdefEntity> {

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 读取资源
     *
     * @param id 流程定义ID
     * @param proInsId 流程实例ID
     * @param type
     * @return
     */
    InputStream readResource(String id, String proInsId, String type);

    /**
     * 上传文件部署
     *
     * @param exportDir
     * @param file
     * @return
     */
    Result deploy(String exportDir, MultipartFile file);

    /**
     * 转模型
     *
     * @param id 流程定义ID
     */
    Model convertToModel(String id);

    /**
     * 开启实例
     *
     * @param processDefinitionId
     */
    void startProcessInstanceById(String processDefinitionId);

    /**
     * 激活/挂起
     *
     * @param id    流程定义ID
     * @param state
     * @return
     */
    Result updateState(String id, Integer state);

    /**
     * 删除流程定义
     *
     * @param deploymentIds 部署ID集合
     */
    void deleteBatch(String[] deploymentIds);
}
