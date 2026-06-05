package com.condominio.edificio.edificio.enums;

import lombok.Getter;

@Getter
public enum EstadoUsuario {
    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO"),
    BLOQUEADO("BLOQUEADO");

    private final String mensaje;

    EstadoUsuario(String mensaje) {
        this.mensaje = mensaje;
    }
}
