package com.condominio.edificio.edificio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoUnidad {
    DEPARTAMENTO("DEPARTAMENTO"),
    COCHERA("COCHERA"),
    DEPOSITO("DEPOSITO"),
    LOCAL("LOCAL");

    private final String descripcion;
}
