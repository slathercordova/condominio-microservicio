package com.condominio.auth.common.enums;

import lombok.Getter;

@Getter
public enum TipoBloqueo {
    ADMINISTRADOR("ADMINISTRADOR"),
    INACTIVIDAD("INACTIVIDAD"),
    OLVIDE_CONTRASEÑA("OLVIDE_CONTRASEÑA"),
    SIN_BLOQUEO("SIN_BLOQUEO"),
    INTENTOS_FALLIDOS("INTENTOS_FALLIDOS");

    private final String mensaje;

    TipoBloqueo(String mensaje) {
        this.mensaje = mensaje;
    }
}
