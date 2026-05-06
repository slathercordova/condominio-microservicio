package com.condominio.persona.controller;

import com.condominio.persona.dto.request.TipoDocumentoRequest;
import com.condominio.persona.dto.response.ApiResponse;
import com.condominio.persona.dto.response.TipoDocumentoDetailResponse;
import com.condominio.persona.dto.response.TipoDocumentoResponse;
import com.condominio.persona.service.TipoDocumentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<TipoDocumentoResponse>> save(@Valid @RequestBody TipoDocumentoRequest tipoDocumentoRequest){
        TipoDocumentoResponse tipoDocumentoResponse = tipoDocumentoService.createTipoDocumento(tipoDocumentoRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Tipo documento creado",null,tipoDocumentoResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id){
        tipoDocumentoService.deleteTipoDocumento(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Tipo documento eliminado",null,null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoDocumentoDetailResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody TipoDocumentoRequest tipoDocumentoRequest){
        TipoDocumentoDetailResponse tipoDocumentoDetailResponse = tipoDocumentoService.updateTipoDocumento(id, tipoDocumentoRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Tipo documento actualizado",null, tipoDocumentoDetailResponse));
    }
}
