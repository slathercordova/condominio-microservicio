package com.condominio.edificio.edificio.enums;

import lombok.Getter;

@Getter
public enum OrigenLedger {
    GASTO_PERIODO_UNIDAD("GASTO_PERIODO_UNIDAD"),
    CARGO_EXTRA("CARGO_EXTRA"),
    PAGO("PAGO"),
    RECIBO("RECIBO"),
    SISTEMA("SISTEMA"),
    AJUSTE("AJUSTE");

    private final String descripcion;

    OrigenLedger(String descripcion) {
        this.descripcion = descripcion;
    }
}
