package com.it.cloud.modules.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.it.cloud.modules.auth.entity.MenuEntity;
import com.it.cloud.modules.auth.entity.RoleMenuEntity;
import com.it.cloud.modules.auth.entity.UserRoleEntity;
import com.it.cloud.modules.auth.entity.dto.RoleMenuDTO;
import com.it.cloud.modules.auth.mapper.RoleMenuMapper;
import com.it.cloud.modules.auth.service.IMenuService;
import com.it.cloud.modules.auth.service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuEntity> implements IRoleMenuService {
    @Autowired
    private IMenuService menuService;

    @Override
    public List<MenuEntity> queryByRoleId(String roleId) {
        List<RoleMenuEntity> roleMenuList = baseMapper.selectList(Wrappers.<RoleMenuEntity>lambdaQuery()
                .eq(RoleMenuEntity::getRoleId, roleId));
        if (CollectionUtil.isEmpty(roleMenuList)) {
            return null;
        }
        //转换成menu id集合
        List<String> menuIdList = roleMenuList.stream().map(item -> item.getMenuId()).collect(Collectors.toList());
        //菜单集合
        List<MenuEntity> menuEntityList = (List<MenuEntity>) menuService.listByIds(menuIdList);

        return menuEntityList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(String roleId, List<String> menuIdList) {
        if (menuIdList == null || menuIdList.size() == 0) {
            return;
        }
        //先删除角色与菜单关系
        deleteBatchByRoleId(new String[]{roleId});

        //保存角色与菜单关系
        for (String menuId : menuIdList) {
            RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
            roleMenuEntity.setMenuId(menuId);
            roleMenuEntity.setRoleId(roleId);

            this.save(roleMenuEntity);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchByRoleId(String[] roleIds) {
        baseMapper.delete(Wrappers.<RoleMenuEntity>lambdaQuery()
                .in(RoleMenuEntity::getRoleId, roleIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allotMenu(RoleMenuDTO roleMenuDTO) {
        List<String> roleList = roleMenuDTO.getMenuList();

        // 删除角色权限
        baseMapper.delete(Wrappers.<RoleMenuEntity>lambdaQuery()
                .eq(RoleMenuEntity::getRoleId, roleMenuDTO.getRoleId()));
        // 添加角色权限
        roleList.forEach(menuId -> {
            this.save(new RoleMenuEntity().setRoleId(roleMenuDTO.getRoleId()).setMenuId(menuId));
        });
    }

}
