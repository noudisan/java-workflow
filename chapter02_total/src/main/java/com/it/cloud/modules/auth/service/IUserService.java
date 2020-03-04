package com.it.cloud.modules.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.modules.auth.entity.UserEntity;
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
public interface IUserService extends IService<UserEntity> {

    /**
     *  分页查询
     * @param params 参数
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     *  条件查询
     * @param userEntity
     * @return
     */
    List<UserEntity> queryList(UserEntity userEntity);

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
