package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.response.ListaEdificiosXUsuarioResponse;
import com.condominio.edificio.edificio.service.UsuarioEdificioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/edificio")
public class UsuarioEdificioController {
    private final UsuarioEdificioService usuarioEdificioService;

    public UsuarioEdificioController(UsuarioEdificioService usuarioEdificioService) {
        this.usuarioEdificioService = usuarioEdificioService;
    }

    @GetMapping("/test")
    public String prueba(){
        return "hola, servicio desde edificio";
    }

    @GetMapping("/usuario-edificio/{idEdificio}/existe")
    public ResponseEntity<ApiResponse<Boolean>> existsUsuarioEdificio(@PathVariable UUID idEdificio){
        Boolean existe = usuarioEdificioService.existsUsuarioEdificio(idEdificio);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Existe?",null,existe));
    }

    @GetMapping("/usuario/mis-edificios")
    public ResponseEntity<ApiResponse<List<ListaEdificiosXUsuarioResponse>>> listaEdificiosPorUsuario(){
        List<ListaEdificiosXUsuarioResponse> respuesta = usuarioEdificioService.listaEdificiosDeUsuario();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Lista de edificios",null,respuesta));
    }
}

