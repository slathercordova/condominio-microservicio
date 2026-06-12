package com.condominio.edificio.edificio.controller;

import com.condominio.edificio.common.response.ApiResponse;
import com.condominio.edificio.edificio.dto.request.PersonaUnidadRequest;
import com.condominio.edificio.edificio.dto.request.UnidadRequest;
import com.condominio.edificio.edificio.dto.response.MisUnidadesResponse;
import com.condominio.edificio.edificio.dto.response.PersonaUnidadResponse;
import com.condominio.edificio.edificio.dto.response.UnidadResponse;
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
}
