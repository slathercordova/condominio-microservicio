package com.condominio.auth.auth.dto.response;

import java.util.UUID;

public record LoginResponse (
    String token,
    UUID id
){
}
