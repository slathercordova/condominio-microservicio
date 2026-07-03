package com.condominio.persona.persona.dto.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class PersonaFilter {
    private UUID id;
    private UUID tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidoPaterno;
    private Boolean estado;
}
