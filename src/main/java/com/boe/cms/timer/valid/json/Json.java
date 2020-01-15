package com.boe.cms.timer.valid.json;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= JsonFormatValidator.class)
@Documented
public @interface Json {

    /**
     * {} 返回true
     * */







    String message() default "json format err";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
