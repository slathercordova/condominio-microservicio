package com.condominio.edificio.edificio.dto.response;

import com.condominio.edificio.edificio.enums.TipoUnidad;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MisUnidadesResponse {
    private UUID idPersonaUnidad;
    private UUID idPersona;
    private String personaNombre;

    private UUID idUnidad;
    private String codigo;
    private BigDecimal metraje;
    private BigDecimal porcentaje;
    private TipoUnidad tipoUnidad;

    private UUID idEdificio;
    private String edificioNombre;
    private String edificioDireccion;
}
