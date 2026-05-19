package com.condominio.auth.auth.service;

import com.condominio.auth.auth.dto.request.LoginRequest;
import com.condominio.auth.auth.dto.request.PersonaRequest;
import com.condominio.auth.auth.dto.request.RegisterRequest;
import com.condominio.auth.auth.dto.response.LoginResponse;
import com.condominio.auth.auth.dto.response.PersonaDetailResponse;
import com.condominio.auth.auth.dto.response.PersonaResponse;
import com.condominio.auth.auth.dto.response.RegisterResponse;
import com.condominio.auth.auth.entity.UsuarioEntity;
import com.condominio.auth.auth.repository.UsuarioRepository;
import com.condominio.auth.common.enums.EstadoUsuario;
import com.condominio.auth.common.enums.TipoBloqueo;
import com.condominio.auth.common.exception.BusinessException;
import com.condominio.auth.common.exception.ExternalServiceException;
import com.condominio.auth.common.exception.ResourceAlreadyExistsException;
import com.condominio.auth.common.exception.ResourceNotFoundException;
import com.condominio.auth.common.response.ApiResponse;
import com.condominio.auth.common.util.DateUtils;
import com.condominio.auth.common.util.SecurityUtils;
import com.condominio.auth.feignclient.PersonaClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;
    private final PersonaClient personaClient;
    private final PasswordEncoder passwordEncoder;
    private static final int MAX_INTENTOS = 5;

    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper modelMapper, SecurityUtils securityUtils, PersonaClient personaClient, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
        this.securityUtils = securityUtils;
        this.personaClient = personaClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        //  Validar que no exista el nombre usuario actualmente
        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("El username ya existe");
        }
        //  Validar que no exista otro usuario con el mismo correo
        if (usuarioRepository.existsByCorreo(registerRequest.getCorreo())) {
            throw new ResourceAlreadyExistsException("El correo ya se encuentra registrado");
        }

        //  Llamar al servicio de persona para traer el ID e identificar si existe o no
        ApiResponse<PersonaDetailResponse> personaDetailResponse = null;
        UUID idPersona = null;
        try {
            personaDetailResponse = personaClient.findPersonaPorDocumento(registerRequest.getTipoDocumento(), registerRequest.getNumeroDocumento());
            log.debug("Respuesta ws persona : {}", personaDetailResponse);
            //  Validar que no exista el id persona con otro usuario ya creado
            idPersona = personaDetailResponse.getData().getId();
            if (usuarioRepository.existsByIdPersona(idPersona)) {
                throw new ResourceAlreadyExistsException("La persona ya tiene un usuario creado");
            }
        } catch (FeignException.NotFound e) {
            log.info("Persona no encontrada, se creará");
            //  llamar al servicio de personas para registrar a la persona de no tener ID persona
            PersonaRequest personaRequest = modelMapper.map(registerRequest, PersonaRequest.class);
            personaRequest.setEstado(true);
            log.debug("Llamando a servicio de persona para crearla : {}", personaRequest);
            ApiResponse<PersonaResponse> personaCreateResponse = personaClient.createPersona(personaRequest);
            idPersona = personaCreateResponse.getData().getId();
            //  Si ya existe persona ya no hay necesidad de llamar al servicio que crea persona
        } catch (FeignException e) {
            log.error("Error consumiendo person-service: status={} body={}", e.status(), e.contentUTF8());
            throw new ExternalServiceException("Error consumiendo el servicio de personas "+e.getMessage());
        }

        //  Si pasa todas las reglas llenar el usuario entity y mandar a crear
        UsuarioEntity userEntity = modelMapper.map(registerRequest, UsuarioEntity.class);
        userEntity.setIdPersona(idPersona);
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntity.setIntentoErroneo(0);
        userEntity.setTipoBloqueo(TipoBloqueo.SIN_BLOQUEO);
        userEntity.setPrimeraVez(true);
        userEntity.setEstado(EstadoUsuario.ACTIVO);
        userEntity.setCreatedBy(securityUtils.getCurrentUserId());

        UsuarioEntity saved = usuarioRepository.save(userEntity);

        //  Si las 2 condiciones anteriores estan ok devolver ok al controller
        return modelMapper.map(saved, RegisterResponse.class);
    }

    @Transactional(noRollbackFor = BusinessException.class)
    public LoginResponse loginUser(LoginRequest loginRequest) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no existe"));

        if (usuarioEntity.getEstado() == EstadoUsuario.ACTIVO) {
            //  Verificar contraseña
            if (passwordEncoder.matches(loginRequest.password(), usuarioEntity.getPassword())) {
                //  Si el usuario se loguea exitosamente reset # intentos, actualizar fecha y hora de último login y devolver token
                usuarioEntity.setIntentoErroneo(0);
                usuarioEntity.setUltimoLogin(DateUtils.nowOffset());
                usuarioEntity.setEstado(EstadoUsuario.ACTIVO);
                usuarioEntity.setTipoBloqueo(TipoBloqueo.SIN_BLOQUEO);
                usuarioEntity.setUpdatedAt(DateUtils.nowOffset());
                usuarioEntity.setCreatedBy(securityUtils.getCurrentUserId());
            } else {
                //  Si falla aumentar el número de intentos
                usuarioEntity.setIntentoErroneo(usuarioEntity.getIntentoErroneo() + 1);
                //  Si se llega a 5 intentos bloquear a usuario
                if (usuarioEntity.getIntentoErroneo() >= MAX_INTENTOS) {
                    usuarioEntity.setEstado(EstadoUsuario.BLOQUEADO);
                    usuarioEntity.setTipoBloqueo(TipoBloqueo.INTENTOS_FALLIDOS);
                    usuarioEntity.setBloqueoAt(DateUtils.nowOffset());
                    usuarioEntity.setUpdatedAt(DateUtils.nowOffset());
                    usuarioEntity.setCreatedBy(securityUtils.getCurrentUserId());
                    throw new BusinessException("Contraseña incorrecta, usuario bloqueado");
                }
                //  Mandar error de contraseña inválida
                throw new BusinessException("Credenciales incorrectas");
            }
        } else {
            throw new BusinessException("Usuario no se encuentra ACTIVO");
        }

        return new LoginResponse("token pendiente", usuarioEntity.getId(), usuarioEntity.isPrimeraVez());
    }
}
