package com.it.cloud.modules.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.cloud.common.constants.SysConstants;
import com.it.cloud.common.enums.MenuTypeEnum;
import com.it.cloud.common.utils.MapUtils;
import com.it.cloud.modules.auth.entity.MenuEntity;
import com.it.cloud.modules.auth.mapper.MenuMapper;
import com.it.cloud.modules.auth.service.IMenuService;
import com.it.cloud.modules.auth.service.IRoleMenuService;
import com.it.cloud.modules.auth.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements IMenuService {
    @Autowired
    private IRoleMenuService roleMenuService;
    @Autowired
    private IUserService userService;

    @Override
    public List<MenuEntity> getUserMenuList(String userId) {
        //系统管理员，拥有最高权限
        if (SysConstants.SUPER_ADMIN.equals(userId)) {
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<String> menuIdList = userService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    @Override
    public List<MenuEntity> queryList(MenuEntity menuEntity) {
        String name = menuEntity.getName();
        List<MenuEntity> menuList = this.list(Wrappers.<MenuEntity>lambdaQuery()
                .eq(StringUtils.isNotBlank(name), MenuEntity::getName, name));

        // 填充父名称
        for (MenuEntity item : menuList) {
            MenuEntity parentMenuEntity = this.getById(item.getParentId());
            if (parentMenuEntity != null) {
                item.setParentName(parentMenuEntity.getName());
            }
        }

        return menuList;
    }

    @Override
    public void delete(String id) {
        //删除菜单
        this.removeById(id);
        //删除菜单与角色关联
        roleMenuService.removeByMap(new MapUtils().put("menu_id", id));
    }

    /**
     * 获取所有目录-菜单列表
     */
    private List<MenuEntity> getAllMenuList(List<String> menuIdList) {
        //查询根菜单列表
        List<MenuEntity> menuList = queryUserListByParentId("0", menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归,构建目录-菜单
     */
    private List<MenuEntity> getMenuTreeList(List<MenuEntity> menuList, List<String> menuIdList) {
        List<MenuEntity> subMenuList = new ArrayList<>();

        for (MenuEntity entity : menuList) {
            if (entity.getType() == MenuTypeEnum.CATALOG.getValue()) {
                entity.setList(getMenuTreeList(queryUserListByParentId(entity.getId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }


    /**
     * 根据父菜单，查询用户子菜单(用于鉴权)
     *
     * @param parentId   父菜单ID
     * @param menuIdList 用户菜单ID
     */
    @Override
    public List<MenuEntity> queryUserListByParentId(String parentId, List<String> menuIdList) {
        List<MenuEntity> menuList = queryListByParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }

        //过滤
        List<MenuEntity> userMenuList = new ArrayList<>();
        for (MenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    /**
     * 根据父菜单，查询所有子菜单
     *
     * @param parentId 父菜单ID
     */
    @Override
    public List<MenuEntity> queryListByParentId(String parentId) {
        return baseMapper.selectList(Wrappers.<MenuEntity>lambdaQuery()
                .eq(MenuEntity::getParentId, parentId)
                .orderByAsc(MenuEntity::getOrderNum)
        );
    }

    @Override
    public List<MenuEntity> queryNotButtonList() {
        return baseMapper.selectList(Wrappers.<MenuEntity>lambdaQuery()
//                .ne(MenuEntity::getType, 2)
                .orderByAsc(MenuEntity::getOrderNum)
        );
    }


}
