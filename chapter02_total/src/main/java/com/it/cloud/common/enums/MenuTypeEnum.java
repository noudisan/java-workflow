package com.it.cloud.common.enums;

import lombok.Getter;

/**
 *  菜单
 *
 * @author 司马缸砸缸了
 * @version 1.0
 * @since 2019-07-12
 * @description error类型枚举类
 */
@Getter
public enum MenuTypeEnum {
    /**
     * 目录
     */
    CATALOG(0),
    /**
     * 菜单
     */
    MENU(1),
    /**
     * 按钮
     */
    BUTTON(2);

    private int value;

    MenuTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
