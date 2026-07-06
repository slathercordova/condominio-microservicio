package com.condominio.edificio.edificio.dto.filter;

import com.condominio.edificio.edificio.enums.TipoAlquiler;
import com.condominio.edificio.edificio.enums.TipoUnidad;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
public class UnidadFilter {
    private UUID idEdificio;
    private UUID id;
    private String codigo;
    private Integer piso;
    private BigDecimal metraje;
    private TipoUnidad tipoUnidad;
    private TipoAlquiler tipoAlquiler;
    private Boolean estado;
}
