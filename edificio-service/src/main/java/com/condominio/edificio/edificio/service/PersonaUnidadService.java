package com.condominio.edificio.edificio.service;

import com.condominio.edificio.common.exception.BusinessException;
import com.condominio.edificio.common.exception.ResourceNotFoundException;
import com.condominio.edificio.common.security.JwtService;
import com.condominio.edificio.edificio.dto.request.PersonaUnidadRequest;
import com.condominio.edificio.edificio.dto.response.MisUnidadesResponse;
import com.condominio.edificio.edificio.dto.response.PersonaDetailResponse;
import com.condominio.edificio.edificio.dto.response.PersonaUnidadResponse;
import com.condominio.edificio.edificio.entity.PersonaUnidadEntity;
import com.condominio.edificio.edificio.repository.MisUnidadesProjection;
import com.condominio.edificio.edificio.repository.PersonaUnidadRepository;
import com.condominio.edificio.edificio.repository.UnidadRepository;
import com.condominio.edificio.feignclient.PersonaClientWs;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class PersonaUnidadService {
    private final PersonaUnidadRepository personaUnidadRepository;
    private final PersonaClientWs personaClientWs;
    private final UnidadRepository unidadRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public PersonaUnidadService(PersonaUnidadRepository personaUnidadRepository, PersonaClientWs personaClientWs, UnidadRepository unidadRepository, ModelMapper modelMapper, JwtService jwtService) {
        this.personaUnidadRepository = personaUnidadRepository;
        this.personaClientWs = personaClientWs;
        this.unidadRepository = unidadRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
    }

    @Transactional
    public PersonaUnidadResponse create(PersonaUnidadRequest personaUnidadRequest) {
        //  Validamos que exista la unidad
        if (!unidadRepository.existsById(personaUnidadRequest.getIdUnidad())) {
            throw new ResourceNotFoundException("La unidad ingresada no existe");
        }

        //  Validamos que exista la persona
        if (personaClientWs.findPersonaById(personaUnidadRequest.getIdPersona()).getData() == null) {
            throw new ResourceNotFoundException("La persona ingresada no existe");
        }

        //  Validamos que la unidad no tenga otro responsable ya asignado
        if (Boolean.TRUE.equals(personaUnidadRequest.getEsResponsable())
                && personaUnidadRepository.existsByIdUnidadAndEsResponsableTrueAndEstadoTrue(personaUnidadRequest.getIdUnidad())) {
            throw new BusinessException("Ya existe otra persona designada como responsable para esta unidad");
        }

        //  Validamos que fecha fin sea posterior a fecha inicio
        if (personaUnidadRequest.getFechaFin() != null) {
            if (personaUnidadRequest.getFechaFin().isBefore(personaUnidadRequest.getFechaInicio())) {
                throw new BusinessException("La fecha fin no puede ser anterior al inicio");
            }
        }

        PersonaUnidadEntity personaUnidadEntity = modelMapper.map(personaUnidadRequest, PersonaUnidadEntity.class);
        personaUnidadEntity.setEsFavorito(false);
        personaUnidadEntity.setEstado(true);

        PersonaUnidadEntity saved = personaUnidadRepository.save(personaUnidadEntity);
        return modelMapper.map(saved, PersonaUnidadResponse.class);
    }

    @Transactional(readOnly = true)
    public List<MisUnidadesResponse> misUnidades(UUID idPersona) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID idPropietario = (UUID) auth.getPrincipal();
        boolean esPropietario = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROPIETARIO"));

        List<MisUnidadesProjection> misUnidadesPro;
        if (esPropietario) {
            misUnidadesPro = personaUnidadRepository.listarMisUnidades(idPropietario);
        } else {
            if (idPersona == null) {
                throw new BusinessException("Debe ingresar un idPersona");
            }
            misUnidadesPro = personaUnidadRepository.listarMisUnidades(idPersona);
        }

        List<MisUnidadesResponse> lista = new ArrayList<>();
        for (MisUnidadesProjection unidad : misUnidadesPro) {
            PersonaDetailResponse persona = personaClientWs.findPersonaById(unidad.getIdPersona()).getData();
            String nombreCompleto = Stream.of(persona.getNombres(), persona.getApellidoPaterno(), persona.getApellidoMaterno())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" "));
            MisUnidadesResponse misUnidadesResponse = modelMapper.map(unidad, MisUnidadesResponse.class);
            misUnidadesResponse.setPersonaNombre(nombreCompleto);
            lista.add(misUnidadesResponse);
        }
        return lista;
    }

    @Transactional
    public void setFavorito(UUID id, boolean esFavorito) {
        PersonaUnidadEntity personaUnidad = personaUnidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id no encontrado"));
        personaUnidad.setEsFavorito(esFavorito);
        personaUnidadRepository.save(personaUnidad);
    }
}
