package com.condominio.edificio.edificio.service;

import com.condominio.edificio.common.exception.BusinessException;
import com.condominio.edificio.common.exception.ResourceNotFoundException;
import com.condominio.edificio.edificio.dto.request.PersonaUnidadRequest;
import com.condominio.edificio.edificio.dto.response.PersonaUnidadResponse;
import com.condominio.edificio.edificio.entity.PersonaUnidadEntity;
import com.condominio.edificio.edificio.repository.PersonaUnidadRepository;
import com.condominio.edificio.edificio.repository.UnidadRepository;
import com.condominio.edificio.feignclient.PersonaClientWs;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PersonaUnidadService {
    private final PersonaUnidadRepository personaUnidadRepository;
    private final PersonaClientWs personaClientWs;
    private final UnidadRepository unidadRepository;
    private final ModelMapper modelMapper;

    public PersonaUnidadService(PersonaUnidadRepository personaUnidadRepository, PersonaClientWs personaClientWs, UnidadRepository unidadRepository, ModelMapper modelMapper) {
        this.personaUnidadRepository = personaUnidadRepository;
        this.personaClientWs = personaClientWs;
        this.unidadRepository = unidadRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public PersonaUnidadResponse create(PersonaUnidadRequest  personaUnidadRequest) {
        //  Validamos que exista la unidad
        if(!unidadRepository.existsById(personaUnidadRequest.getIdUnidad())){
            throw new ResourceNotFoundException("La unidad ingresada no existe");
        }

        //  Validamos que exista la persona
        if(personaClientWs.findPersonaById(personaUnidadRequest.getIdPersona()).getData()==null){
            throw new ResourceNotFoundException("La persona ingresada no existe");
        }

        //  Validamos que la unidad no tenga otro responsable ya asignado
        if(Boolean.TRUE.equals(personaUnidadRequest.getEsResponsable())
            && personaUnidadRepository.existsByIdUnidadAndEsResponsableTrueAndEstadoTrue(personaUnidadRequest.getIdUnidad())){
            throw new BusinessException("Ya existe otra persona designada como responsable para esta unidad");
        }

        //  Validamos que fecha fin sea posterior a fecha inicio
        if (personaUnidadRequest.getFechaFin()!=null){
            if (personaUnidadRequest.getFechaFin().isBefore(personaUnidadRequest.getFechaInicio())){
                throw new BusinessException("La fecha fin no puede ser anterior al inicio");
            }
        }

        PersonaUnidadEntity personaUnidadEntity = modelMapper.map(personaUnidadRequest, PersonaUnidadEntity.class);
        personaUnidadEntity.setEstado(true);

        PersonaUnidadEntity saved = personaUnidadRepository.save(personaUnidadEntity);
        return modelMapper.map(saved, PersonaUnidadResponse.class);
    }
}
