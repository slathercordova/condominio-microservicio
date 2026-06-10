package com.condominio.edificio.common.validation;

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
@NotBlank(message = "Password es obligatorio")
@Size(
        min = 8,
        max = 100,
        message = "El password debe tener entre 8 y 100 caracteres"
)
@Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9])\\S{8,100}$",
        message = "Password Formato inválido: debe contener, - una minúscula - una mayúscula - un número - un carácter especial - entre 8 y 100 caracteres - sin espacios"
)
public @interface ValidPassword {

    String message() default "Password inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}