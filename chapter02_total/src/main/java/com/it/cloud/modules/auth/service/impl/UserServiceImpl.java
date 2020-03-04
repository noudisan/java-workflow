package com.it.cloud.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.common.utils.Query;
import com.it.cloud.modules.auth.entity.UserEntity;
import com.it.cloud.modules.auth.mapper.UserMapper;
import com.it.cloud.modules.auth.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String)params.get("username");
        String sex = (String)params.get("sex");
        String mobile = (String)params.get("mobile");
        Integer minAge = (Integer)params.get("minAge");
        Integer maxAge = (Integer)params.get("maxAge") == null ? 200 : (Integer)params.get("maxAge");

        IPage<UserEntity> page = this.page(new Query<UserEntity>(params).getPage(), Wrappers.<UserEntity>lambdaQuery()
                .eq(StringUtils.isNotBlank(username), UserEntity::getUsername, username)
                .eq(StringUtils.isNotBlank(sex), UserEntity::getSex, sex)
                .like(StringUtils.isNotBlank(mobile), UserEntity::getMobile, mobile)
                .between(minAge != null, UserEntity::getAge, minAge, maxAge)
                .orderByDesc(UserEntity::getCreateTime));

        return new PageUtils(page);
    }

    @Override
    public List<UserEntity> queryList(UserEntity userEntity) {
        List<UserEntity> list = baseMapper.selectList(Wrappers.<UserEntity>lambdaQuery()
                .eq(StringUtils.isNotBlank(userEntity.getUsername()), UserEntity::getUsername, userEntity.getUsername())
                .eq(StringUtils.isNotBlank(userEntity.getSex()), UserEntity::getSex, userEntity.getSex())
                .orderByDesc(UserEntity::getCreateTime));

        return list;
    }

    @Override
    public List<String> queryAllPerms(String userId) {
        return baseMapper.queryAllPerms(userId);
    }

    @Override
    public List<String> queryAllMenuId(String userId) {
        return baseMapper.queryAllMenuId(userId);
    }

}
