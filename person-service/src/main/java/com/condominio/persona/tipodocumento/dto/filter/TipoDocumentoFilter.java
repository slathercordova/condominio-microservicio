package com.condominio.persona.tipodocumento.dto.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class TipoDocumentoFilter {
    private UUID id;
    private String nombre;
    private String nombreCorto;
    private Boolean estado; //  Tiene que ser en maýuscula para determinar si filtrar o no en nulo
}
