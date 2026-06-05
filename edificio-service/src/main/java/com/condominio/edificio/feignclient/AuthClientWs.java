package com.condominio.edificio.feignclient;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.response.RegisterResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "auth-client",
        url = "${auth.url}",
        configuration = FeignConfig.class)

public interface AuthClientWs {
    @GetMapping("/api/v1/auth/{id}")
    ApiResponse<RegisterResponse> findAuthById(@PathVariable UUID id);
}
