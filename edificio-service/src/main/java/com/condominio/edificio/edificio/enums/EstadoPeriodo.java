package com.condominio.edificio.edificio.enums;

import lombok.Getter;

@Getter
public enum EstadoPeriodo {
    BORRADOR("BORRADOR"),
    GENERADO("GENERADO"),
    CERRADO("CERRADO"),
    ANULADO("ANULADO");

    private final String descripcion;

    EstadoPeriodo(String descripcion) {
        this.descripcion = descripcion;
    }
}
