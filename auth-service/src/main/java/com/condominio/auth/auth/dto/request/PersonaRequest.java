package com.condominio.auth.auth.dto.request;

import com.condominio.auth.common.enums.TipoSexo;
import com.condominio.auth.common.validation.ValidCelular;
import com.condominio.auth.common.validation.ValidCorreo;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonaRequest {
    @NotNull(message = "Tipo documento obligatorio")
    private UUID tipoDocumento;

    @NotBlank(message = "Número documento obligatorio")
    @Size(max = 20, message = "Número de documento máximo 20 caracteres")
    private String numeroDocumento;

    @NotNull(message = "Fecha de nacimiento es obligatorio")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate nacimiento;

    @Size(max = 20, message = "Celular máximo 20 caracteres")
    @ValidCelular
    private String celular;

    @Size(max = 100, message = "Correo máximo 100 caracteres")
    @ValidCorreo
    private String correo;

    //@NotBlank(message = "Nombres obligatorio")
    @Size(max = 30, message = "Nombres máximo 30 caracteres")
    private String nombres;

    //@NotBlank(message = "Apellido paterno obligatorio")
    @Size(max = 30, message = "Apellido paterno máximo 30 caracteres")
    private String apellidoPaterno;

    //@NotBlank(message = "Apellido materno obligatorio")
    @Size(max = 30, message = "Apellido materno máximo 30 caracteres")
    private String apellidoMaterno;

    @NotNull(message = "Sexo es obligatorio")
    private TipoSexo sexo;

    @NotNull(message = "Estado es obligatorio")
    private Boolean estado;
}
