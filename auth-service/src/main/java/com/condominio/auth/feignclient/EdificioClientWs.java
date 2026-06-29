package com.condominio.auth.feignclient;

import com.condominio.auth.auth.dto.response.EdificioDetailResponse;
import com.condominio.auth.auth.dto.response.RolResponse;
import com.condominio.auth.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(
//        name = "edificio-client",
//        url = "${edificio.url}",
        name = "EDIFICIO-SERVICE",
        configuration = FeignSecurityConfig.class)

public interface EdificioClientWs {
    @GetMapping("/api/v1/usuedirol/usuario/{idUsuario}/edificio/{idEdificio}/roles")
    ApiResponse<List<RolResponse>> findRolesByUsuarioAndEdificio(@PathVariable UUID idUsuario, @PathVariable UUID idEdificio);

    @GetMapping("/api/v1/edificio/usuario-edificio/{idEdificio}/existe")
    ResponseEntity<ApiResponse<Boolean>> existsUsuarioEdificio(@PathVariable UUID idEdificio);

    @GetMapping("/api/v1/edificio/{id}")
    ResponseEntity<ApiResponse<EdificioDetailResponse>> getDetailEdificio(@PathVariable UUID id);
}
