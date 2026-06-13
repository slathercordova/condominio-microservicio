package com.condominio.edificio.feignclient;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.response.PersonaDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
//        name = "person-client",
//        url = "${person.url}",
        name = "PERSON-SERVICE",
        configuration = FeignSecurityConfig.class)

public interface PersonaClientWs {
    @GetMapping("/api/v1/persona/documento")
    ApiResponse<PersonaDetailResponse> findPersonaPorDocumento(
            @RequestParam UUID tipoDocumento,
            @RequestParam String numeroDocumento);

    @GetMapping("/api/v1/persona/{id}")
    ApiResponse<PersonaDetailResponse> findPersonaById(@PathVariable UUID id);
}
