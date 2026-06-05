package com.condominio.edificio.edificio.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioEdificioRolResponse {
    private UUID id;
    private UUID idUsuario;
    private UUID idEdificio;
    private UUID idRol;
    private Boolean estado;
}
