package com.condominio.persona.persona.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonaResponse {
    private UUID id;
    private UUID tipoDocumentoId;
    private String tipoDocumentoNombre;
    private String numeroDocumento;
}
