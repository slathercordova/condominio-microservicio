package com.condominio.edificio.edificio.dto.response;

import com.condominio.edificio.edificio.enums.TipoPropiedad;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonaUnidadResponse {
    private UUID id;
    private UUID idUnidad;
    private UUID idPersona;
    private TipoPropiedad tipoPropiedad;
}
