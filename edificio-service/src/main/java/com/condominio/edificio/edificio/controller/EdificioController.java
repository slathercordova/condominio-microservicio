package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.request.EdificioRequest;
import com.condominio.edificio.edificio.dto.response.EdificioResponse;
import com.condominio.edificio.edificio.service.EdificioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/edificio")
public class EdificioController {
    private final EdificioService edificioService;
    public EdificioController(EdificioService edificioService) {
        this.edificioService = edificioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<EdificioResponse>> create(@Valid @RequestBody EdificioRequest edificioRequest){
        EdificioResponse Response = edificioService.create(edificioRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Edificio creado exitosamente", null, Response));
    }
}
