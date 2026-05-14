package com.condominio.auth.auth.dto.request;

import com.condominio.auth.common.enums.TipoSexo;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    //  Datos necesarios para el registro de persona tras ws
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

    @Size(max = 100, message = "Correo máximo 100 caracteres")
    @Email
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "El correo debe tener un dominio válido"
    )
    private String correo;

    @Size(max = 30, message = "Nombres máximo 30 caracteres")
    private String nombres;

    @Size(max = 30, message = "Apellido paterno máximo 30 caracteres")
    private String apellidoPaterno;

    @Size(max = 30, message = "Apellido materno máximo 30 caracteres")
    private String apellidoMaterno;

    @NotNull(message = "Sexo es obligatorio")
    private TipoSexo sexo;

    //  Datos necesario para el registro de usuario
    @NotBlank(message = "Usuario es obligatorio")
    @Size(max = 30, message = "Usuario máximo 30 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "Username inválido"
    )
    private String username;

    @NotBlank(message = "Password es obligatorio")
    @Size(max = 100, message = "Password máximo 100 caracteres")
    @Size(min = 8, max = 100, message = "El password debe tener entre 8 y 100 caracteres")
    private String password;
}
