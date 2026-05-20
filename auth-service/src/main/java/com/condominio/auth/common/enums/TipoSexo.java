package com.condominio.auth.common.enums;

import lombok.Getter;

//todo realizar webservice de este enum
@Getter
public enum TipoSexo {
    MASCULINO("MASCULINO"),
    FEMENINO("FEMENINO");

    private final String mensaje;

    TipoSexo(String mensaje) {
        this.mensaje = mensaje;
    }
}
