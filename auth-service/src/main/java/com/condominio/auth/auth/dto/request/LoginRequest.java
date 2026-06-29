package com.condominio.auth.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Usuario es obligatorio")
        String username,

        @NotBlank(message = "Password es obligatorio")
        String password
) {
}
