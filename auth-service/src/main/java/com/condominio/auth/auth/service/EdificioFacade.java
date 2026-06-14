package com.condominio.auth.auth.service;

import com.condominio.auth.auth.dto.response.RolResponse;
import com.condominio.auth.common.exception.ExternalServiceException;
import com.condominio.auth.common.response.ApiResponse;
import com.condominio.auth.feignclient.EdificioClientWs;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EdificioFacade {
    private final EdificioClientWs edificioClientWs;

    public EdificioFacade(EdificioClientWs edificioClientWs) {
        this.edificioClientWs = edificioClientWs;
    }

    //  fallbacks

    @Retry(name = "edificioService")
    @CircuitBreaker(
            name = "edificioService",
            fallbackMethod = "fallbackExistsUsuarioEdificio")
    public boolean validarUsuarioEdificio(UUID idEdificio) {
        log.info("Entró a validarUsuarioEdificio");
        return edificioClientWs.existsUsuarioEdificio(idEdificio).getBody().getData();
    }

    public boolean fallbackExistsUsuarioEdificio(UUID idEdificio,Throwable ex) {
        log.error("fallbackExistsUsuarioEdificio activado: {}", ex.getMessage());
        throw new ExternalServiceException(
                "Servicio de edificio temporalmente no disponible"
        );
    }

    @Retry(name = "edificioService")
    @CircuitBreaker(
            name = "edificioService",
            fallbackMethod = "fallbackRoles")
    public ApiResponse<List<RolResponse>> obtenerRoles(UUID idUsuario, UUID idEdificio) {
        return edificioClientWs.findRolesByUsuarioAndEdificio(idUsuario,idEdificio);
    }

    public ApiResponse<List<RolResponse>> fallbackRoles(UUID idUsuario,UUID idEdificio,Throwable ex) {
        log.error("fallbackRoles activado: {}", ex.getMessage());
        return new ApiResponse<>(false,"Servicio de edificio temporalmente no disponible",null,List.of());
    }
}
