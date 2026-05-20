package com.condominio.auth.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "Debe de enviar el refresh token")
        String refreshToken
) {
}
