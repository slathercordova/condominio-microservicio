package com.condominio.edificio.edificio.dto.request;

import com.condominio.edificio.common.validation.ValidCelular;
import com.condominio.edificio.common.validation.ValidCorreo;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EdificioRequest {
    @NotBlank(message = "Debe ingresar un RUC")
    @Size(max = 11, message = "Ruc máximo 11 dígitos")
    private String ruc;

    @Size(max = 20, message = "Telefono máximo 20 dígitos")
    private String telefono;

    @ValidCelular
    private String celular;

    @ValidCorreo
    private String correo;

    @NotNull(message = "Debe ingresar el tipo documento representate")
    private UUID tipoDocumentoRepre;

    @NotBlank(message = "Debe ingresar el número documento representate")
    private String numeroDocumentoRepre;

    @Size(max = 300, message = "Logo Url máximo 300 caracteres")
    private String logoUrl;
}
