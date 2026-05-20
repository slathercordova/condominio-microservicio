package com.condominio.auth.auth.dto.response;

import java.util.UUID;

public record LoginResponse (
    String accessToken,
    String refreshToken,
    UUID id,
    boolean primeraVez
){
}
