package com.it.cloud.modules.auth.mapper;

import com.it.cloud.modules.auth.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     * @return
     */
    List<String> queryAllPerms(String userId);

    /**
     * 查询用户的所有菜单ID
     * @param userId
     * @return
     */
    List<String> queryAllMenuId(String userId);
}
