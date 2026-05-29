package com.condominio.edificio.common.enums;

import lombok.Getter;

@Getter
public enum TipoCobro {
    PORCENTAJE("PORCENTAJE"),
    FLAT("FLAT"),
    CONSUMO("CONSUMO");

    private final String descripcion;

    TipoCobro(String descripcion) {
        this.descripcion = descripcion;
    }
}
