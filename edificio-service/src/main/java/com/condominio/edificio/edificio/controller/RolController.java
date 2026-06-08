package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.request.RolRequest;
import com.condominio.edificio.edificio.dto.response.RolResponse;
import com.condominio.edificio.edificio.service.RolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rol")
public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<RolResponse>> registrarRol(@Valid @RequestBody RolRequest rolRequest){
        RolResponse rolResponse = rolService.createRol(rolRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Registro exitoso", null, rolResponse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<RolResponse>> editRol(
            @PathVariable UUID id,
            @Valid @RequestBody RolRequest rolRequest){
        RolResponse RolResponse = rolService.updateRol(id,rolRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Update exitoso", null, RolResponse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<Void>> eliminarRol(@PathVariable UUID id){
        rolService.deleteRol(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "Eliminado", null, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RolResponse>>> listarAll(){
        List<RolResponse> lista = rolService.getAllRol();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Lista",null, lista));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RolResponse>> getRol(@PathVariable UUID id){
        RolResponse rolResponse = rolService.getRol(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Rol encontrado",null, rolResponse));
    }

}
