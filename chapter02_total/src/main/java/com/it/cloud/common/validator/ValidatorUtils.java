package com.it.cloud.common.validator;

import com.it.cloud.common.exceptions.YYException;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * ValidatorUtils
 *
 * @author 司马缸砸缸了
 * @date 2019-07-14
 * @description
 */
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws YYException 校验不通过，则报YYException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws YYException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            List<String> collect = constraintViolations.stream()
                    .map(constant -> constant.getMessage())
                    .collect(Collectors.toList());
            String msg = StringUtils.join(collect, ",");
            throw new YYException(msg);
        }
    }
}
