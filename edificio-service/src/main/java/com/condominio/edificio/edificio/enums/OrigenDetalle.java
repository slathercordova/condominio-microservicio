package com.condominio.edificio.edificio.enums;

import lombok.Getter;

@Getter
public enum OrigenDetalle {
    GASTO_PERIODO("GASTO_PERIODO"),
    CARGO_EXTRA("CARGO_EXTRA");

    private final String descripcion;

    OrigenDetalle(String descripcion) {
        this.descripcion = descripcion;
    }
}
