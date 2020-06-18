package com.dztt.activiti.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dztt.activiti.entity.ActIdUser;
import com.dztt.activiti.mapper.ActIdUserMapper;
import com.dztt.activiti.service.IActIdUserService;
import org.springframework.stereotype.Service;

/**
 * Created by zhoutaotao on 2019/12/17.
 */
@Service
public class ActIdUserServiceImpl extends ServiceImpl<ActIdUserMapper, ActIdUser> implements IActIdUserService {
}
