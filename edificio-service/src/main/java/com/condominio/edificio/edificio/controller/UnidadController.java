package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.request.PersonaUnidadRequest;
import com.condominio.edificio.edificio.dto.request.UnidadRequest;
import com.condominio.edificio.edificio.dto.response.*;
import com.condominio.edificio.edificio.service.PersonaUnidadService;
import com.condominio.edificio.edificio.service.UnidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/unidad")
public class UnidadController {
    private final UnidadService unidadService;
    private final PersonaUnidadService personaUnidadService;

    public UnidadController(UnidadService unidadService, PersonaUnidadService personaUnidadService) {
        this.unidadService = unidadService;
        this.personaUnidadService = personaUnidadService;
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<UnidadResponse>> addUnidad(@Valid @RequestBody UnidadRequest unidadRequest){
        UnidadResponse unidadResponse = unidadService.create(unidadRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Unidad creada",null,unidadResponse));
    }

    @PostMapping("/asignar-persona")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<PersonaUnidadResponse>> create(@Valid @RequestBody PersonaUnidadRequest personaUnidadRequest){
        PersonaUnidadResponse respuesta = personaUnidadService.create(personaUnidadRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Se asigno a la unidad el propietario exitosamente", null, respuesta));
    }

    @GetMapping("/mis-unidades/{idPersona}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION','PROPIETARIO')")
    public ResponseEntity<ApiResponse<List<MisUnidadesResponse>>> misUnidades(@PathVariable UUID idPersona){
        List<MisUnidadesResponse> respuesta = personaUnidadService.misUnidades(idPersona);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Lista de unidades", null, respuesta));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION','PROPIETARIO')")
    public ResponseEntity<ApiResponse<UnidadDetailResponse>> unidadDetail(@PathVariable UUID id){
        UnidadDetailResponse respuesta = unidadService.detailUnidad(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Detalle de unidad", null, respuesta));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> deleteUnidad(@PathVariable UUID id) {
        unidadService.deleteUnidad(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "Unidad eliminada", null, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<UnidadDetailResponse>> updateUnidad(
            @PathVariable UUID id,
            @Valid @RequestBody UnidadRequest unidadRequest) {
        UnidadDetailResponse unidadDetailResponse = unidadService.updateUnidad(id, unidadRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Unidad actualizada", null, unidadDetailResponse));
    }
}
