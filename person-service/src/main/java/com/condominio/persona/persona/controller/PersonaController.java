package com.condominio.persona.persona.controller;

import com.condominio.persona.common.response.ApiResponse;
import com.condominio.persona.persona.dto.request.PersonaRequest;
import com.condominio.persona.persona.dto.response.PersonaDetailResponse;
import com.condominio.persona.persona.dto.response.PersonaResponse;
import com.condominio.persona.persona.service.PersonaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/persona")
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<PersonaResponse>> addPersona(@Valid @RequestBody PersonaRequest personaRequest){
        PersonaResponse res = personaService.createPersona(personaRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "persona creada", null, res));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<PersonaResponse>> deletePersona(@PathVariable UUID id){
        personaService.deletePersona(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(true, "persona eliminada", null, null));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','ADMINISTRACION')")
    public ResponseEntity<ApiResponse<PersonaDetailResponse>> updatePersona(
            @PathVariable UUID id,
            @Valid @RequestBody PersonaRequest personaRequest){
        PersonaDetailResponse personaDetailResponse =  personaService.updatePersona(id, personaRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "persona actualizada", null, personaDetailResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonaDetailResponse>> findById(@PathVariable UUID id){
        PersonaDetailResponse personaDetailResponse = personaService.findPersona(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "persona encontrada", null, personaDetailResponse));
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsPersonaPorDocumento(
            @RequestParam() UUID tipoDocumento,
            @RequestParam() String numeroDocumento){
        boolean existe = personaService.existsPersonaPorDocumento(tipoDocumento, numeroDocumento);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Consulta exitosa",null,existe));
    }

    @GetMapping("/documento")
    public ResponseEntity<ApiResponse<PersonaDetailResponse>> findPersonaPorDocumento(
            @RequestParam() UUID tipoDocumento,
            @RequestParam() String numeroDocumento){
        PersonaDetailResponse personaDetailResponse = personaService.findPersonaPorDocumento(tipoDocumento, numeroDocumento);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"Consulta exitosa",null,personaDetailResponse));
    }
}
