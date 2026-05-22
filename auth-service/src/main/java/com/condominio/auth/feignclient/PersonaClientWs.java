package com.condominio.auth.feignclient;

import com.condominio.auth.auth.dto.request.PersonaRequest;
import com.condominio.auth.auth.dto.response.PersonaDetailResponse;
import com.condominio.auth.auth.dto.response.PersonaResponse;
import com.condominio.auth.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
        name = "person-client",
        url = "${person.url}",
        configuration = FeignConfig.class)

public interface PersonaClientWs {
    @GetMapping("/api/v1/persona/documento")
    ApiResponse<PersonaDetailResponse> findPersonaPorDocumento(
            @RequestParam UUID tipoDocumento,
            @RequestParam String numeroDocumento);

    @PostMapping("/api/v1/persona")
    ApiResponse<PersonaResponse> createPersona(@Valid @RequestBody PersonaRequest personaRequest);
}
