package com.condominio.persona.tipodocumento.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TipoDocumentoRequest {
    @NotBlank(message = "Nombre es obligatorio")
    @Size(max = 50, message = "Nombre máximo 50 caracteres")
    private String nombre;

    @NotBlank(message = "Nombre corto es obligatorio")
    @Size(max = 5, message = "Nombre corto máximo 5 caracteres")
    private String nombreCorto;

    @NotNull(message = "Estado es obligatorio")
    private Boolean estado;
}

