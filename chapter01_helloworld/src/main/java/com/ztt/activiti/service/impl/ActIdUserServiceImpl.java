package com.ztt.activiti.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ztt.activiti.entity.ActIdUser;
import com.ztt.activiti.mapper.ActIdUserMapper;
import com.ztt.activiti.service.IActIdUserService;
import org.springframework.stereotype.Service;

/**
 * Created by zhoutaotao on 2019/12/17.
 */
@Service
public class ActIdUserServiceImpl extends ServiceImpl<ActIdUserMapper, ActIdUser> implements IActIdUserService {
}
