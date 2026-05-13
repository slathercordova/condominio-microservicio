package com.condominio.persona.persona.dto.request;

import com.condominio.persona.common.enums.TipoSexo;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    @Pattern(
            regexp = "^[0-9]{9}$",
            message = "Celular inválido"
    )
    private String celular;

    @Size(max = 20, message = "Celular2 máximo 20 caracteres")
    @Pattern(
            regexp = "^[0-9]{9}$",
            message = "Celular 2 inválido"
    )
    private String celular2;

    @Size(max = 100, message = "Correo máximo 100 caracteres")
    @Email
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "El correo debe tener un dominio válido"
    )
    private String correo;

    @Size(max = 100, message = "Correo2 máximo 100 caracteres")
    @Email
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "El correo2 debe tener un dominio válido"
    )
    private String correo2;

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
