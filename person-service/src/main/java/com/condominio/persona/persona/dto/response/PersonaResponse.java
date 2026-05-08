package com.condominio.persona.persona.dto.response;

import com.condominio.persona.tipodocumento.entity.TipoDocumentoEntity;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonaResponse {
    private UUID id;
    private TipoDocumentoEntity tipoDocumento;
    private String numeroDocumento;
}
