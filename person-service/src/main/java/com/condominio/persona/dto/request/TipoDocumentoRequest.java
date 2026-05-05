package com.condominio.persona.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoDocumentoRequest {
    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Nombre corto es obligatorio")
    private String nombreCorto;

    private boolean estado;
}

