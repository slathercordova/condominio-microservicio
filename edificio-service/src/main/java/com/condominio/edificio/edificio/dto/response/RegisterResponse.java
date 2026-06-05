package com.condominio.edificio.edificio.dto.response;

import com.condominio.edificio.edificio.enums.EstadoUsuario;
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
