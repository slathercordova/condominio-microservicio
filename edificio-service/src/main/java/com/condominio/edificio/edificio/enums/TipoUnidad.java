package com.condominio.edificio.edificio.enums;

public enum TipoUnidad {
    DEPARTAMENTO("DEPARTAMENTO"),
    COCHERA("COCHERA"),
    DEPOSITO("DEPOSITO"),
    LOCAL("LOCAL");

    private final String descripcion;

    TipoUnidad(String descripcion) {
        this.descripcion = descripcion;
    }
}
