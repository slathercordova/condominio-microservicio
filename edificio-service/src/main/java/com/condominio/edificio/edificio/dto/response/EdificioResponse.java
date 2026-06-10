package com.condominio.edificio.edificio.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EdificioResponse {
    private UUID id;
    private String razonSocial;
    private String direccion;
    private String ruc;
}
