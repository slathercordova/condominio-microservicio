package com.condominio.auth.feignclient;

import com.condominio.auth.auth.dto.response.PersonaDetailResponse;
import com.condominio.auth.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "person-client", url = "${person.url}")
public interface PersonaClient {
    @GetMapping("/api/v1/persona/documento")
    ApiResponse<PersonaDetailResponse> findPersonaPorDocumento(
            @RequestParam UUID tipoDocumento,
            @RequestParam String numeroDocumento);
}
