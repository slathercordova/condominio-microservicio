package com.condominio.auth.auth.dto.response;

import com.condominio.auth.common.enums.TipoSexo;

import java.util.List;
import java.util.UUID;

public record LoginEdificioResponse (
        String accessToken,
        String refreshToken,
        UUID idUsuario,
        boolean primeraVez,
        boolean edificioSeleccionado,
        UUID idPersona,
        String nombres,
        String apellidoPaterno,
        String apellidoMaterno,
        String nombreCompleto,
        TipoSexo sexo,
        List<RolResponse> roles,
        UUID idEdificio,
        String nombreEdificio
){

}
