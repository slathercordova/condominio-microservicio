package com.condominio.edificio.common.enums;

import lombok.Getter;

@Getter
public enum TipoConcepto {
    ORDINARIO("ORDINARIO"),
    EXTRAORDINARIO("EXTRAORDINARIO");

    private final String descripcion;

    TipoConcepto(String descripcion) {
        this.descripcion = descripcion;
    }
}
