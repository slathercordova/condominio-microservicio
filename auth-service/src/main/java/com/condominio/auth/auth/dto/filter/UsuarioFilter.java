package com.condominio.auth.auth.dto.filter;

import com.condominio.auth.auth.enums.EstadoUsuario;
import com.condominio.auth.auth.enums.TipoBloqueo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class UsuarioFilter {
    private UUID id;
    private UUID idPersona;
    private String username;
    private String correo;
    private TipoBloqueo tipoBloqueo;
    private EstadoUsuario estado;
}
