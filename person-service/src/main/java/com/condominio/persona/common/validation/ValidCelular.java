package com.condominio.persona.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(
        regexp = "^[0-9]{9}$",
        message = "Formato celular inválido"
)
public @interface ValidCelular {
    String message() default "Celular inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
