package com.condominio.edificio.edificio.dto.response;

import com.condominio.edificio.edificio.enums.TipoUnidad;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UnidadResponse {
    private UUID id;
    private UUID idEdificio;
    private String torre;
    private Integer piso;
    private String codigo;
    private TipoUnidad tipoUnidad;
    private BigDecimal metraje;
}
