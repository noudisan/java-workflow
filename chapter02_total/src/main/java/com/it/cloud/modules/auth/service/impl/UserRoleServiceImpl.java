package com.it.cloud.modules.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.it.cloud.modules.auth.entity.UserRoleEntity;
import com.it.cloud.modules.auth.entity.dto.UserRoleDTO;
import com.it.cloud.modules.auth.mapper.UserRoleMapper;
import com.it.cloud.modules.auth.service.IUserRoleService;
import org.springframework.stereotype.Service;

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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements IUserRoleService {

    @Override
    public void deleteBatchByRoleId(String[] roleIds) {
        baseMapper.delete(Wrappers.<UserRoleEntity>lambdaQuery()
                .in(UserRoleEntity::getRoleId, roleIds));
    }

    @Override
    public List<String> queryByUserId(String userId) {
        List<UserRoleEntity> userRoleList = this.list(Wrappers.<UserRoleEntity>lambdaQuery().eq(UserRoleEntity::getUserId, userId));
        //转换成menu id集合
        List<String> roleIdList = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(userRoleList)) {
            roleIdList = userRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        }

        return roleIdList;
    }

    @Override
    public void allotRole(UserRoleDTO userRoleDTO) {
        List<String> roleList = userRoleDTO.getRoleList();

        // 删除用户角色
        baseMapper.delete(Wrappers.<UserRoleEntity>lambdaQuery()
                .in(UserRoleEntity::getUserId, userRoleDTO.getUserId()));
        // 添加
        roleList.forEach(roleId -> {
            this.save(new UserRoleEntity().setUserId(userRoleDTO.getUserId()).setRoleId(roleId));
        });
    }

}
