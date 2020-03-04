package com.it.cloud.common.validator.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author 司马缸砸缸了
 * @description Ip注解处理器
 * @date 2019-7-23
 */
public class IpValidator implements ConstraintValidator<Ip, String> {
    /**
     * ip的正则表达式
     */
    private String ipReg =
        "^(([1-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))\\.)(([1-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))\\.){2}([1-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))$";
    private Pattern ipPattern = Pattern.compile(ipReg);

    @Override
    public void initialize(Ip constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return ipPattern.matcher(value).matches();
    }

}
