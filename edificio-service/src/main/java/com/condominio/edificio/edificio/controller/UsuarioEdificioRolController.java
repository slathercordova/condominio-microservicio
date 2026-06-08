package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.request.UsuarioEdificioRolRequest;
import com.condominio.edificio.edificio.dto.response.RolResponse;
import com.condominio.edificio.edificio.dto.response.UsuarioEdificioRolResponse;
import com.condominio.edificio.edificio.service.UsuarioEdificioRolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuedirol")
public class UsuarioEdificioRolController {
    private final UsuarioEdificioRolService usuarioEdificioRolService;

    public UsuarioEdificioRolController(UsuarioEdificioRolService usuarioEdificioRolService) {
        this.usuarioEdificioRolService = usuarioEdificioRolService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<UsuarioEdificioRolResponse>> create(@Valid @RequestBody UsuarioEdificioRolRequest usuarioEdificioRolRequest){
        UsuarioEdificioRolResponse response = usuarioEdificioRolService.create(usuarioEdificioRolRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new  ApiResponse<>(true,"Registro exitoso",null,response));
    }

    @GetMapping
    public  ResponseEntity<ApiResponse<List<UsuarioEdificioRolResponse>>> getAll(){
        List<UsuarioEdificioRolResponse> list = usuarioEdificioRolService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Lista",null,list));
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ApiResponse<UsuarioEdificioRolResponse>> get(@PathVariable UUID id){
        UsuarioEdificioRolResponse uer = usuarioEdificioRolService.find(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Registro encontrado",null,uer));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id){
        usuarioEdificioRolService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new  ApiResponse<>(true,"Eliminado",null,null));
    }

    @GetMapping("/usuario/{idUsuario}/edificio/{idEdificio}")
    public ResponseEntity<ApiResponse<List<RolResponse>>> findRolesByUsuarioAndEdificio(
            @PathVariable UUID idUsuario,@PathVariable UUID idEdificio){
        List<RolResponse> listaRoles = usuarioEdificioRolService.findRolesByUsuarioAndEdificio(idUsuario,idEdificio);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new  ApiResponse<>(true,"Lista de Roles",null,listaRoles));
    }
}
