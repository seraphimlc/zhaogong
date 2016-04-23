package com.dagong.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;


/**
 * Created by lc on 16/2/3.
 */
public class BeanValidator {

    public static <T> void validate(T object) throws ValidationException{

        //获得验证器
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        //执行验证
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        //如果有验证信息，则将第一个取出来包装成异常返回
        ConstraintViolation<T> constraintViolation = getFirst(constraintViolations, null);
        if (constraintViolation != null) {
            throw new ValidationException(constraintViolation.getPropertyPath()+constraintViolation.getMessage());
        }
    }



}
