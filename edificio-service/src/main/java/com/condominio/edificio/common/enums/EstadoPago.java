package com.condominio.edificio.common.enums;

public enum EstadoPago {
    REGISTRADO("REGISTRADO"),
    CONFIRMADO("CONFIRMADO"),
    ANULADO("ANULADO");

    private final String descripcion;

    EstadoPago(String descripcion) {
        this.descripcion = descripcion;
    }
}
