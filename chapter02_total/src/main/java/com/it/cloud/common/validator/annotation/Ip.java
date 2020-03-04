package com.it.cloud.common.validator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 司马缸砸缸了
 * @description IP校验注解
 * @date 2019/7/14 15:44
 */

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = IpValidator.class )
public @interface Ip {

	String message() default "IP格式不正确";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };


}
