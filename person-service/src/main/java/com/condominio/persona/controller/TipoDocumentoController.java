package com.condominio.persona.controller;

import com.condominio.persona.dto.request.TipoDocumentoRequest;
import com.condominio.persona.dto.response.ApiResponse;
import com.condominio.persona.dto.response.TipoDocumentoResponse;
import com.condominio.persona.service.TipoDocumentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/person")
public class TipoDocumentoController {
    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<TipoDocumentoResponse>> save(@Valid @RequestBody TipoDocumentoRequest tipoDocumentoRequest){
        TipoDocumentoResponse tipoDocumentoResponse = tipoDocumentoService.createTipoDocumento(tipoDocumentoRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Tipo documento creado",null,tipoDocumentoResponse));
    }

    @GetMapping("/testServicio")
    public String test(){
        return "Respuesta del servicio nuevo creado donde no funciona el post";
    }
}
