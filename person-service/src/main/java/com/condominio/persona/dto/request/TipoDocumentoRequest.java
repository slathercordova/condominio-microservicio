package com.condominio.persona.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TipoDocumentoRequest {
    @NotBlank(message = "Nombre es obligatorio")
    @Size(max = 50, message = "Nombre máximo 50 caracteres")
    private String nombre;

    @NotBlank(message = "Nombre corto es obligatorio")
    @Size(max = 5, message = "Nombre corto máximo 5 caracteres")
    private String nombreCorto;

    private boolean estado;
}

