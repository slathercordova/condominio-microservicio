package com.condominio.persona.dto.response;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TipoDocumentoResponse {
    private UUID id;
    private String nombre;
}
