package com.it.cloud.common.annotation;

import java.lang.annotation.*;


/**
 * <p>
 * 系统日志注解
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-15
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";
}
