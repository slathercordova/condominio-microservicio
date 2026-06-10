package com.condominio.edificio.edificio.dto.response;

import com.condominio.edificio.common.enums.TipoSexo;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDetailResponse {
    private UUID id;

    private UUID tipoDocumentoId;
    private String tipoDocumentoNombre;

    private String numeroDocumento;
    private LocalDate nacimiento;
    private String celular;
    private String celular2;
    private String correo;
    private String correo2;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private TipoSexo sexo;
    private Boolean estado;
}
