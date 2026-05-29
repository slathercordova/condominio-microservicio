package com.condominio.edificio.common.enums;

import lombok.Getter;

@Getter
public enum EstadoRecibo {
    PENDIENTE("PENDIENTE"),
    PAGADO("PAGADO"),
    VENCIDO("VENCIDO"),
    ANULADO("ANULADO");

    private final String descripcion;

    EstadoRecibo(String descripcion) {
        this.descripcion = descripcion;
    }
}
