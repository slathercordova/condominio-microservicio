package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.request.EdificioRequest;
import com.condominio.edificio.edificio.dto.response.EdificioDetailResponse;
import com.condominio.edificio.edificio.dto.response.EdificioResponse;
import com.condominio.edificio.edificio.dto.response.RegisterResponse;
import com.condominio.edificio.edificio.service.EdificioService;
import com.condominio.edificio.edificio.service.UnidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/edificio")
public class EdificioController {
    private final EdificioService edificioService;
    private final UnidadService unidadService;
    public EdificioController(EdificioService edificioService, UnidadService unidadService) {
        this.edificioService = edificioService;
        this.unidadService = unidadService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<EdificioResponse>> create(@Valid @RequestBody EdificioRequest edificioRequest){
        EdificioResponse Response = edificioService.create(edificioRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Edificio creado exitosamente", null, Response));
    }

    @PostMapping("/{idEdificio}/calcular-porcentajes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<Integer>> calcularPorcentajes(@PathVariable UUID idEdificio){
        Integer modificados = unidadService.calcularPorcentajes(idEdificio);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Se recalcularon "+modificados+" unidades",null,modificados));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EdificioDetailResponse>> findById(@PathVariable UUID id){
        EdificioDetailResponse respuesta = edificioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Edificio encontrado", null, respuesta));
    }
}
