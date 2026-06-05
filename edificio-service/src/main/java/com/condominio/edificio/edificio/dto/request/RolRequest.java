package com.condominio.edificio.edificio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RolRequest {
    @NotBlank(message = "Debe ingresar un nombre")
    @Size(max = 30, message = "Nombre máximo 30 caracteres")
    private String nombre;

    @NotNull(message = "Debe ingresar un estado")
    private Boolean estado;
}
