package com.condominio.edificio.common.enums;

import lombok.Getter;

@Getter
public enum MetodoPago {
    EFECTIVO("EFECTIVO"),
    TRANSFERENCIA("TRANSFERENCIA"),
    YAPE("YAPE"),
    PLIN("PLIN"),
    TARJETA("TARJETA"),
    OTRO("OTRO");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }
}
