package com.condominio.edificio.common.enums;

import lombok.Getter;

@Getter
public enum TipoPropiedad {
    PROPIETARIO("PROPIETARIO"),
    COPROPIETARIO("COPROPIETARIO"),
    APODERADO("APODERADO"),
    INQUILINO("INQUILINO");

    private final String descripcion;

    TipoPropiedad(String descripcion) {
        this.descripcion = descripcion;
    }
}
