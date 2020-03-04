package com.it.cloud.modules.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Sets;
import com.it.cloud.common.constants.RedisKeyConstants;
import com.it.cloud.common.constants.SysConstants;
import com.it.cloud.modules.auth.entity.MenuEntity;
import com.it.cloud.modules.auth.entity.RoleEntity;
import com.it.cloud.modules.auth.entity.UserEntity;
import com.it.cloud.modules.auth.entity.UserTokenEntity;
import com.it.cloud.modules.auth.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * IShiroService 实现类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Service
public class ShiroServiceImpl implements IShiroService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserTokenService userTokenService;

    @Autowired
    private IUserRoleService userRoleService;

    /**
     * 获取用户的所有权限
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> getUserPermissions(String userId) {
        Set<String> permSet = Sets.newHashSet();
        List<MenuEntity> menuList = null;
        List<String> permsList;
        //系统管理员，拥有最高权限
        if (SysConstants.SUPER_ADMIN.equals(userId)) {
            menuList = menuService.list();
        } else {
            //用户菜单列表
            List<String> menuIdList = userService.queryAllMenuId(userId);
            if (CollectionUtil.isNotEmpty(menuIdList)) {
                menuList = (List<MenuEntity>) menuService.listByIds(menuIdList);
            }
        }

        //得到用户权限列表
        if (CollectionUtil.isNotEmpty(menuList)) {
            permsList = menuList.stream()
                    //.filter(item -> StringUtils.isNotBlank(item.getPerms()))
                    .map(MenuEntity::getPerms)
                    .collect(Collectors.toList());
            //得到用户权限列表
            if (CollectionUtil.isNotEmpty(permsList)) {
                permSet = permsList.stream()
                        // 过滤空置的字符串
                        .filter(perms -> StringUtils.isNotEmpty(perms))
                        // 把小的list合并成大的list
                        .flatMap(perms -> Arrays.stream(perms.split(",")))
                        // 转换成set集合
                        .collect(Collectors.toSet());
            }
        }

        return permSet;
    }

    @Override
    public Set<String> getUserRoles(String userId) {
        Set<String> roleSet = Sets.newHashSet();
        List<RoleEntity> roleList = null;
        //系统管理员，拥有最高权限
        if (SysConstants.SUPER_ADMIN.equals(userId)) {
            roleList = roleService.list();
        } else {
            //用户角色列表
            List<String> roleIdList = userRoleService.queryByUserId(userId);
            if (CollectionUtil.isNotEmpty(roleIdList)) {
                roleList = (List<RoleEntity>) roleService.listByIds(roleIdList);
            }
        }

        //得到用户角色英文名称列表
        if (CollectionUtil.isNotEmpty(roleList)) {
            roleSet = roleList.stream()
                    .map(RoleEntity::getRoleName)
                    .collect(Collectors.toSet());
        }

        return roleSet;
    }

    /**
     * 查询token
     *
     * @param token
     * @return
     */
    @Override
    public UserTokenEntity queryByToken(String token) {
        return userTokenService.queryByToken(RedisKeyConstants.MANAGE_SYS_USER_TOKEN + token);
    }

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity queryUser(String userId) {
        return userService.getById(userId);
    }

    /**
     * 续期
     *
     * @param userId
     * @param accessToken
     */
    @Override
    public void refreshToken(String userId, String accessToken) {
        userTokenService.refreshToken(userId, accessToken);
    }

}
