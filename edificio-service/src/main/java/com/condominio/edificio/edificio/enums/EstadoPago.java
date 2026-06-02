package com.condominio.edificio.edificio.enums;

public enum EstadoPago {
    REGISTRADO("REGISTRADO"),
    CONFIRMADO("CONFIRMADO"),
    ANULADO("ANULADO");

    private final String descripcion;

    EstadoPago(String descripcion) {
        this.descripcion = descripcion;
    }
}
