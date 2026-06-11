package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.request.UnidadRequest;
import com.condominio.edificio.edificio.dto.response.UnidadResponse;
import com.condominio.edificio.edificio.service.UnidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/unidad")
public class UnidadController {
    private final UnidadService unidadService;

    public UnidadController(UnidadService unidadService) {
        this.unidadService = unidadService;
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<UnidadResponse>> addUnidad(@Valid @RequestBody UnidadRequest unidadRequest){
        UnidadResponse unidadResponse = unidadService.create(unidadRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Unidad creada",null,unidadResponse));
    }
}
