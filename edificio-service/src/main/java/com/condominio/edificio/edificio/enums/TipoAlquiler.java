package com.condominio.edificio.edificio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoAlquiler {
    ALQUILER("ALQUILER"),
    AIRBNB("AIRBNB");

    private final String descripcion;
}
