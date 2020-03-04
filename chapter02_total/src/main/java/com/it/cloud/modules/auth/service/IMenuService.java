package com.it.cloud.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.cloud.modules.auth.entity.MenuEntity;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
public interface IMenuService extends IService<MenuEntity> {

    /**
     * 根据父菜单，查询用户子菜单
     *
     * @param parentId   父菜单ID
     * @param menuIdList 用户菜单ID
     * @return
     */
    List<MenuEntity> queryUserListByParentId(String parentId, List<String> menuIdList);

    /**
     * 根据父菜单，查询所有子菜单
     *
     * @param parentId 父菜单ID
     * @return
     */
    List<MenuEntity> queryListByParentId(String parentId);

    /**
     * 获取不包含按钮的菜单列表
     *
     * @return
     */
    List<MenuEntity> queryNotButtonList();

    /**
     * 根据用户ID获取用户菜单列表
     *
     * @param userId
     * @return
     */
    List<MenuEntity> getUserMenuList(String userId);

    /**
     * 条件查询
     *
     * @param menuEntity
     * @return
     */
    List<MenuEntity> queryList(MenuEntity menuEntity);

    /**
     * 删除
     *
     * @param id
     */
    void delete(String id);

}
