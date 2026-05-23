package com.condominio.auth.auth.dto.request;

import com.condominio.auth.common.validation.ValidCorreo;

public record ForgotPasswordRequest (
        @ValidCorreo
        String correo
) {
}
