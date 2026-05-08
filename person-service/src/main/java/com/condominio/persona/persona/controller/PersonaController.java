package com.condominio.persona.persona.controller;

import com.condominio.persona.persona.dto.request.PersonaRequest;
import com.condominio.persona.persona.dto.response.PersonaResponse;
import com.condominio.persona.persona.service.PersonaService;
import com.condominio.persona.tipodocumento.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/persona")
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PersonaResponse>> addPersona(@Valid @RequestBody PersonaRequest personaRequest){
        PersonaResponse res = personaService.createPersona(personaRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "persona creada", null, res));
    }
}
