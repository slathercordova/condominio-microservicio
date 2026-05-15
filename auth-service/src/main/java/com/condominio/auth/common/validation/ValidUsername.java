package com.condominio.auth.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank(message = "Usuario es obligatorio")
@Size(min = 4, max = 30, message = "Usuario min 4 máx 30 caracteres")
@Pattern(
        regexp = "^[a-zA-Z0-9._-]+$",
        message = """
                Username inválido
                Valores permitidos a-Z 0-9 -_.
                """
)
public @interface ValidUsername {
    String message() default "Username inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
