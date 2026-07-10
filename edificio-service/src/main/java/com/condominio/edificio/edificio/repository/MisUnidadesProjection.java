package com.condominio.edificio.edificio.repository;

import com.condominio.edificio.edificio.enums.TipoUnidad;

import java.math.BigDecimal;
import java.util.UUID;

public interface MisUnidadesProjection {
    UUID getIdPersonaUnidad();
    UUID getIdPersona();
    Boolean getEsFavorito();

    UUID getIdUnidad();
    String getCodigo();
    BigDecimal getMetraje();
    BigDecimal getPorcentaje();
    TipoUnidad getTipoUnidad();
    BigDecimal getDeudaTmp();

    UUID getIdEdificio();
    String getEdificioNombre();
    String getEdificioDireccion();
}
