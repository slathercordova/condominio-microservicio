package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.response.CatalogoResponse;
import com.condominio.edificio.edificio.service.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogos")
public class CatalogoController {
    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/tipos-unidad")
    public ResponseEntity<ApiResponse<List<CatalogoResponse>>> tiposUnidad() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Lista de tipos de unidad",
                        null,
                        catalogoService.listarTiposUnidad()
                )
        );
    }

    @GetMapping("/tipos-alquiler")
    public ResponseEntity<ApiResponse<List<CatalogoResponse>>> tiposAlquiler() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Lista de tipos de alquiler",
                        null,
                        catalogoService.listarTiposAlquiler()
                )
        );
    }
}
