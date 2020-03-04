package com.it.cloud.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.cloud.modules.auth.entity.UserRoleEntity;
import com.it.cloud.modules.auth.entity.dto.UserRoleDTO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
public interface IUserRoleService extends IService<UserRoleEntity> {

    /**
     * 批量删除，根据roleId
     *
     * @param roleIds
     * @return
     */
    void deleteBatchByRoleId(String[] roleIds);

    /**
     * 查询用户已有角色
     *
     * @param userId
     * @return
     */
    List<String> queryByUserId(String userId);

    /**
     * 分配角色
     *
     * @param userEntity
     */
    void allotRole(UserRoleDTO userRoleDTO);
}
