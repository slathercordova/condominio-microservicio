package com.condominio.persona.tipodocumento.dto.response;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TipoDocumentoResponse {
    private UUID id;
    private String nombre;
}
