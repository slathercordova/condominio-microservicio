package com.condominio.edificio.edificio.dto.request;

import com.condominio.edificio.edificio.enums.TipoPropiedad;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonaUnidadRequest {

    @NotNull(message = "Debe ingresar el ID de la unidad")
    private UUID idUnidad;

    @NotNull(message = "Debe ingresar el ID de persona")
    private UUID idPersona;

    @NotNull(message = "Debe indicar si es o no responsable")
    private Boolean esResponsable;

    @NotNull(message = "Debe indicar la fecha de inicio")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @NotNull(message = "Debe indicar el tipo de propiedad")
    private TipoPropiedad tipoPropiedad;

    @NotNull(message = "Debe indicar el estado")
    private Boolean estado;
}
