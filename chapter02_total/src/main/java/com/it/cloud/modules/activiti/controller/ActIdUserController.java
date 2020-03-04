package com.it.cloud.modules.activiti.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  如果要使用组任务，就添加个同步机制。（可以使用候选人代替组任务，省去同步麻烦）
 *  同步业务用户，角色，用户角色关系
 *  对应ACTIVITI 用户，组，用户组关系
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-09-16
 */
@Controller
@RequestMapping("/activiti/actIdUser")
public class ActIdUserController {

}
