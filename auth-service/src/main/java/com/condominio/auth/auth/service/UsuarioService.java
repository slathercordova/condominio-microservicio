package com.condominio.auth.auth.service;

import com.condominio.auth.auth.dto.request.RegisterRequest;
import com.condominio.auth.auth.dto.response.PersonaDetailResponse;
import com.condominio.auth.auth.dto.response.RegisterResponse;
import com.condominio.auth.auth.entity.UsuarioEntity;
import com.condominio.auth.auth.repository.UsuarioRepository;
import com.condominio.auth.common.enums.EstadoUsuario;
import com.condominio.auth.common.enums.TipoBloqueo;
import com.condominio.auth.common.exception.ResourceAlreadyExistsException;
import com.condominio.auth.common.response.ApiResponse;
import com.condominio.auth.common.util.SecurityUtils;
import com.condominio.auth.feignclient.PersonaClient;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;
    private final PersonaClient personaClient;

    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper, SecurityUtils securityUtils, PersonaClient personaClient) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.securityUtils = securityUtils;
        this.personaClient = personaClient;
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        //  Validar que no exista el nombre usuario actualmente
        if(usuarioRepository.existsByUsername(registerRequest.getUsername())){
            throw new ResourceAlreadyExistsException("El username ya existe");
        }
        //  Validar que no exista otro usuario con el mismo correo
        if (usuarioRepository.existsByCorreo(registerRequest.getCorreo())) {
            throw new ResourceAlreadyExistsException("El correo ya se encuentra registrado");
        }

        //  Llamar al servicio de persona para traer el ID e identificar si existe o no
        ApiResponse<PersonaDetailResponse> personaDetailResponse = personaClient.findPersonaPorDocumento(registerRequest.getTipoDocumento(), registerRequest.getNumeroDocumento());
        log.debug("Respuesta ws persona : {}", personaDetailResponse);
        //  Validar que no exista el id persona con otro usuario ya creado
        if (usuarioRepository.existsByIdPersona(personaDetailResponse.getData().getId())){
            throw new ResourceAlreadyExistsException("La persona ya tiene un usuario creado");
        }

        //  Si pasa todas las reglas llenar el usuario entity y mandar a crear
        UsuarioEntity userEntity = modelMapper.map(registerRequest, UsuarioEntity.class);
        userEntity.setIdPersona(personaDetailResponse.getData().getId());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
        userEntity.setIntentoErroneo((short) 0);
        userEntity.setTipoBloqueo(TipoBloqueo.SIN_BLOQUEO);
        userEntity.setPrimeraVez(true);
        userEntity.setEstado(EstadoUsuario.ACTIVO);
        userEntity.setCreatedBy(securityUtils.getCurrentUserId());

        UsuarioEntity saved = usuarioRepository.save(userEntity);

        //  llamar al servicio de personas para registrar a la persona de no tener ID persona
        if (personaDetailResponse.getData().getId() == null) {
            System.out.println("Llamar al servicio para registrar persona");
        }
        //  Si ya existe persona ya no hay necesidad de llamar al servicio que crea persona

        //  Si las 2 condiciones anteriores estan ok devolver ok al controller
        return modelMapper.map(saved, RegisterResponse.class);
    }
}
