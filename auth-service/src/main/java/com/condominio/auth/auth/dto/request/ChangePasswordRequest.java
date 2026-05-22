package com.condominio.auth.auth.dto.request;

import com.condominio.auth.common.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest (
        @NotBlank(message = "Contraseña antigua no puede estar vacía")
        String oldPassword,
        @ValidPassword
        String newPassword
){
}
