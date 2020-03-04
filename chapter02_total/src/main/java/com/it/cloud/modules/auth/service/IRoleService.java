package com.it.cloud.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.modules.auth.entity.RoleEntity;
import com.it.cloud.modules.auth.entity.dto.RoleDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
public interface IRoleService extends IService<RoleEntity> {

    /**
     *  分页查询
     * @param params 参数
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     *  分页查询
     * @param params 参数
     * @return
     */
    List<RoleEntity> queryByUser(String userId);

    /**
     * 添加角色和角色-菜单关系
     * @param roleDto
     * @return
     */
    boolean save(RoleDTO roleDto);

    /**
     * 更新角色
     * @param roleDto
     * @return
     */
    boolean updateById(RoleDTO roleDto);

    /**
     * 批量删除
     * @param roleIds
     */
    void deleteBatch(String[] roleIds);

}
