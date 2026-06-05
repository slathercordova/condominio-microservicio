package com.condominio.edificio.edificio.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioEdificioRolRequest {
    @NotNull(message = "El Usuario es obligatorio")
    private UUID idUsuario;

    @NotNull(message = "El Edificio es obligatorio")
    private UUID idEdificio;

    @NotNull(message = "El Rol es obligatorio")
    private UUID idRol;
}
