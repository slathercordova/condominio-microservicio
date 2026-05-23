package com.condominio.auth.auth.dto.request;

import com.condominio.auth.common.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest (
        @NotBlank
        String token,

        @ValidPassword
        String password
) {
}
