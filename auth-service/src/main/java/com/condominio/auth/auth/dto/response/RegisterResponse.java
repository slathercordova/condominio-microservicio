package com.condominio.auth.auth.dto.response;

import com.condominio.auth.common.enums.EstadoUsuario;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterResponse {
    private UUID id;
    private String username;
    private EstadoUsuario estado;
}
