package com.it.cloud.modules.activiti.service;

import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.modules.activiti.entity.ActReModelEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IActReModelService {

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加
     *
     * @param actReModel
     * @return
     */
    String save(ActReModelEntity actReModel);

    /**
     * 部署
     *
     * @param id
     * @return
     */
    void deploy(String id);

    /**
     * 导出
     *
     * @param id
     * @param response
     */
    void export(String id, String type, HttpServletResponse response);

    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteBatch(String[] ids);
}
