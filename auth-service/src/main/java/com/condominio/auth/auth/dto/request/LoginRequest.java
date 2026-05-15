package com.condominio.auth.auth.dto.request;

import com.condominio.auth.common.validation.ValidPassword;
import com.condominio.auth.common.validation.ValidUsername;

public record LoginRequest(
        @ValidUsername
        String username,

        @ValidPassword
        String password
) {
}
