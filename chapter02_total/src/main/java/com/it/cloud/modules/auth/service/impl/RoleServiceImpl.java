package com.it.cloud.modules.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.common.utils.Query;
import com.it.cloud.modules.auth.entity.RoleEntity;
import com.it.cloud.modules.auth.entity.dto.RoleDTO;
import com.it.cloud.modules.auth.mapper.RoleMapper;
import com.it.cloud.modules.auth.service.IRoleMenuService;
import com.it.cloud.modules.auth.service.IRoleService;
import com.it.cloud.modules.auth.service.IUserRoleService;
import com.it.cloud.modules.auth.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements IRoleService {
    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String roleName = (String) params.get("roleName");
        String createUser = (String) params.get("createUser");

        IPage<RoleEntity> page = baseMapper.selectPage(new Query<RoleEntity>(params).getPage(),
                Wrappers.<RoleEntity>lambdaQuery()
                        .like(StringUtils.isNotBlank(roleName), RoleEntity::getRoleName, roleName)
                        .eq(StringUtils.isNotBlank(createUser), RoleEntity::getCreateUser, createUser)
        );

        return new PageUtils(page);
    }

    @Override
    public List<RoleEntity> queryByUser(String userId) {
        List<String> roleIdList = userRoleService.queryByUserId(userId);
        if(CollectionUtil.isEmpty(roleIdList)){
            return null;
        }
        List<RoleEntity> roleList = (List<RoleEntity>) this.listByIds(roleIdList);
        return roleList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(RoleDTO roleDto) {
        //菜单ID集合
        List<String> menuIdList = roleDto.getMenuIdList();

        RoleEntity role = new RoleEntity();
        BeanUtil.copyProperties(roleDto, role);
        roleDto.setCreateTime(DateUtil.date().toTimestamp());
        baseMapper.insert(role);

        //保存角色与菜单关系
        roleMenuService.saveOrUpdate(role.getId(), menuIdList);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(RoleDTO roleDto) {
        //菜单ID集合
        List<String> menuIdList = roleDto.getMenuIdList();

        RoleEntity role = new RoleEntity();
        BeanUtil.copyProperties(roleDto, role);
        baseMapper.updateById(role);

        if (menuIdList != null && menuIdList.size() != 0) {
            //保存角色与菜单关系
            roleMenuService.saveOrUpdate(role.getId(), menuIdList);
        }
        return true;
    }

    @Override
    public void deleteBatch(String[] ids) {
        //删除角色
        this.removeByIds(Arrays.asList(ids));

        //删除角色与菜单关联
        roleMenuService.deleteBatchByRoleId(ids);

        //删除角色与用户关联
        userRoleService.deleteBatchByRoleId(ids);
    }
}
