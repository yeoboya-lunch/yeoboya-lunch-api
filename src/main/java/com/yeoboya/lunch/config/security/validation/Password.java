package com.yeoboya.lunch.config.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

    String message() default "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}