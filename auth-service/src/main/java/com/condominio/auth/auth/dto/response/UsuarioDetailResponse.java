package com.condominio.auth.auth.dto.response;

import com.condominio.auth.auth.enums.EstadoUsuario;
import com.condominio.auth.auth.enums.TipoBloqueo;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDetailResponse {
    private UUID id;
    private UUID idPersona;
    private String username;
    private String correo;
    private String correo2;
    private Instant ultimoLogin;
    private Integer intentoErroneo;
    private Instant bloqueoAt;
    private TipoBloqueo tipoBloqueo;
    private boolean primeraVez;
    private EstadoUsuario estado;
}
