package com.condominio.persona.tipodocumento.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class TipoDocumentoDetailResponse {
    private UUID id;
    private String nombre;
    private String nombreCorto;
    private boolean estado;
}
