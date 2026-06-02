package com.condominio.edificio.edificio.enums;

import lombok.Getter;

@Getter
public enum PeriodoMora {
    DIARIO("DIARIO"),
    SEMANAL("SEMANAL"),
    MENSUAL("MENSUAL");

    private final String descripcion;

    PeriodoMora(String descripcion) {
        this.descripcion = descripcion;
    }
}
