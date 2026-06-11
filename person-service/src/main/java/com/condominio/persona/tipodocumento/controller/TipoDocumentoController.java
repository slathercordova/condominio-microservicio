package com.condominio.persona.tipodocumento.controller;

import com.condominio.persona.common.response.ApiResponse;
import com.condominio.persona.tipodocumento.dto.filter.TipoDocumentoFilter;
import com.condominio.persona.tipodocumento.dto.request.TipoDocumentoRequest;
import com.condominio.persona.tipodocumento.dto.response.TipoDocumentoDetailResponse;
import com.condominio.persona.tipodocumento.dto.response.TipoDocumentoResponse;
import com.condominio.persona.tipodocumento.service.TipoDocumentoService;
import com.condominio.persona.common.pagination.PaginatedResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tipo-documento")
public class TipoDocumentoController {
    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<TipoDocumentoResponse>> save(@Valid @RequestBody TipoDocumentoRequest tipoDocumentoRequest){
        TipoDocumentoResponse tipoDocumentoResponse = tipoDocumentoService.createTipoDocumento(tipoDocumentoRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Tipo documento creado",null,tipoDocumentoResponse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id){
        tipoDocumentoService.deleteTipoDocumento(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true,"Tipo documento eliminado",null,null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<TipoDocumentoDetailResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TipoDocumentoRequest tipoDocumentoRequest){
        TipoDocumentoDetailResponse tipoDocumentoDetailResponse = tipoDocumentoService.updateTipoDocumento(id, tipoDocumentoRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Tipo documento actualizado",null, tipoDocumentoDetailResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoDocumentoDetailResponse>> findById(@PathVariable UUID id){
        TipoDocumentoDetailResponse tipoDocumentoDetailResponse = tipoDocumentoService.getTipoDocumento(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Tipo documento encontrado",null, tipoDocumentoDetailResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<TipoDocumentoDetailResponse>>> findAll(
            TipoDocumentoFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        PaginatedResponse<TipoDocumentoDetailResponse> result = tipoDocumentoService.findByFilters(filter, page, size, sortBy, direction);

        return ResponseEntity.ok(new ApiResponse<>(true,"Lista encontrada",null,result));
    }
}
