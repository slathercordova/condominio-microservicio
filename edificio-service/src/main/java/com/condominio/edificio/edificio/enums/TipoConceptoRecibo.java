package com.condominio.edificio.edificio.enums;

import lombok.Getter;

@Getter
public enum TipoConceptoRecibo {
    SERVICIO_BASICOS("SERVICIO_BASICOS"),
    MANTENIMIENTO_EQUIPOS("MANTENIMIENTO_EQUIPOS"),
    ADMINISTRACION("ADMINISTRACION"),
    MORA("MORA"),
    MULTA("MULTA"),
    PENALIDAD("PENALIDAD");

    private final String descripcion;

    TipoConceptoRecibo(String descripcion) {
        this.descripcion = descripcion;
    }
}
