package com.condominio.edificio.edificio.dto.response;

import com.condominio.edificio.edificio.enums.TipoAlquiler;
import com.condominio.edificio.edificio.enums.TipoUnidad;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UnidadDetailResponse {
    private UUID idEdificio;
    private UUID id;
    private String codigo;
    private String logoUrl;
    private Integer piso;
    private String torre;
    private BigDecimal metraje;
    private BigDecimal porcentaje;
    private TipoUnidad tipoUnidad;
    private TipoAlquiler tipoAlquiler;
    private Boolean estado;
}
