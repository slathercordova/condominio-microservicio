package com.condominio.edificio.edificio.enums;

import lombok.Getter;

@Getter
public enum TipoEventoLedger {
    RECIBO_GENERADO("RECIBO_GENERADO"),
    CARGO_EXTRA("CARGO_EXTRA"),
    MORA_APLICADA("MORA_APLICADA"),
    PAGO_REGISTRADO("PAGO_REGISTRADO"),
    REVERSO_PAGO("REVERSO_PAGO"),
    RECIBO_ANULADO("RECIBO_ANULADO"),
    AJUSTE_MANUAL("AJUSTE_MANUAL");

    private final String descripcion;

    TipoEventoLedger(String descripcion) {
        this.descripcion = descripcion;
    }
}
