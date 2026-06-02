package com.condominio.edificio.edificio.enums;

import lombok.Getter;

@Getter
public enum TipoAlquiler {
    ALQUILER("ALQUILER"),
    AIRBNB("AIRBNB");

    private final String descripcion;

    TipoAlquiler(String descripcion) {
        this.descripcion = descripcion;
    }
}
