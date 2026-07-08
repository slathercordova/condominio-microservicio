package com.condominio.edificio.edificio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoPropiedad {
    PROPIETARIO("PROPIETARIO"),
    COPROPIETARIO("COPROPIETARIO"),
    APODERADO("APODERADO"),
    INQUILINO("INQUILINO");

    private final String descripcion;
}
