package com.condominio.auth.auth.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RolResponse {
    private UUID id;
    private String nombre;
    private Boolean estado;
}
