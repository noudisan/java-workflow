package com.it.cloud.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.cloud.modules.auth.entity.MenuEntity;
import com.it.cloud.modules.auth.entity.RoleMenuEntity;
import com.it.cloud.modules.auth.entity.dto.RoleMenuDTO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
public interface IRoleMenuService extends IService<RoleMenuEntity> {
    /**
     * 得到角色对应的所有菜单
     *
     * @param roleId
     * @return
     */
    List<MenuEntity> queryByRoleId(String roleId);

    /**
     * 存储或更新关系
     *
     * @param roleId
     * @param menuIdList
     */
    void saveOrUpdate(String roleId, List<String> menuIdList);

    /**
     * 批量删除, 根据roleId
     *
     * @param roleIds
     */
    void deleteBatchByRoleId(String[] roleIds);

    /**
     * 分配菜单
     *
     * @param roleMenuDTO
     */
    void allotMenu(RoleMenuDTO roleMenuDTO);
}
