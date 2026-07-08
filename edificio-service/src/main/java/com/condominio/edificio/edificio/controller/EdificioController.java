package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.pagination.PaginatedResponse;
import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.filter.EdificioFilter;
import com.condominio.edificio.edificio.dto.request.EdificioRequest;
import com.condominio.edificio.edificio.dto.response.EdificioDetailResponse;
import com.condominio.edificio.edificio.dto.response.EdificioResponse;
import com.condominio.edificio.edificio.service.EdificioService;
import com.condominio.edificio.edificio.service.UnidadService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
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
    public ResponseEntity<ApiResponse<EdificioResponse>> create(@Valid @RequestBody EdificioRequest edificioRequest) {
        EdificioResponse Response = edificioService.create(edificioRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Edificio creado exitosamente", null, Response));
    }

    @PostMapping("/{idEdificio}/calcular-porcentajes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<Integer>> calcularPorcentajes(@PathVariable UUID idEdificio) {
        Integer modificados = unidadService.calcularPorcentajes(idEdificio);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Se recalcularon " + modificados + " unidades", null, modificados));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EdificioDetailResponse>> findById(@PathVariable UUID id) {
        EdificioDetailResponse respuesta = edificioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Edificio encontrado", null, respuesta));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> deleteEdificio(@PathVariable UUID id) {
        edificioService.deleteEdificio(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "edificio eliminado", null, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<EdificioDetailResponse>> updateEdificio(
            @PathVariable UUID id,
            @Valid @RequestBody EdificioRequest edificioRequest) {
        EdificioDetailResponse response = edificioService.updateEdificio(id, edificioRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Edificio actualizado", null, response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<EdificioDetailResponse>>> findFilters(
            EdificioFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        PaginatedResponse<EdificioDetailResponse> result = edificioService.findByFilters(filter, page, size, sortBy, direction);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista", null, result));
    }
}
