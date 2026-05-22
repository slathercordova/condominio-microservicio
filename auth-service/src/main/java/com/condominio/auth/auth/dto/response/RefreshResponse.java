package com.condominio.auth.auth.dto.response;

import java.util.UUID;

public record RefreshResponse(
        String accessToken,
        String refreshToken,
        UUID id
) {
}
