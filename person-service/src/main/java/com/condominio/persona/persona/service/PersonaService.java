package com.condominio.persona.persona.service;

import com.condominio.persona.common.exception.ExternalServiceException;
import com.condominio.persona.common.exception.ResourceAlreadyExistsException;
import com.condominio.persona.common.exception.ResourceNotFoundException;
import com.condominio.persona.common.exception.ValidationException;
import com.condominio.persona.common.util.DatosConstant;
import com.condominio.persona.common.util.SecurityUtils;
import com.condominio.persona.feignClient.ReniecClient;
import com.condominio.persona.feignClient.ReniecResponseDto;
import com.condominio.persona.persona.dto.request.PersonaRequest;
import com.condominio.persona.persona.dto.response.PersonaDetailResponse;
import com.condominio.persona.persona.dto.response.PersonaResponse;
import com.condominio.persona.persona.entity.PersonaEntity;
import com.condominio.persona.persona.repository.PersonaRepository;
import com.condominio.persona.tipodocumento.entity.TipoDocumentoEntity;
import com.condominio.persona.tipodocumento.repository.TipoDocumentoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class PersonaService {
    private final PersonaRepository personaRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final ReniecClient reniecClient;
    @Value("${api.token}")
    private String apiToken;

    public PersonaService(PersonaRepository personaRepository, ModelMapper modelMapper, SecurityUtils securityUtils, TipoDocumentoRepository tipoDocumentoRepository, ReniecClient reniecClient) {
        this.personaRepository = personaRepository;
        this.modelMapper = modelMapper;
        this.securityUtils = securityUtils;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.reniecClient = reniecClient;
    }

    @Transactional
    public PersonaResponse createPersona(PersonaRequest personaRequest) {
        log.info("Iniciando persona request");
        log.debug("Request : {}", personaRequest);

        if(personaRepository.existsByCorreo(personaRequest.getCorreo())){
            throw new ResourceAlreadyExistsException("El correo ingresado ya existe");
        }

        //  procesamos dni
        TipoDocumentoEntity  tipDocEnt = tipoDocumentoRepository.findById(personaRequest.getTipoDocumento())
                .orElseThrow(()-> new ResourceNotFoundException("Tipo Documento no encontrado"));

        if (personaRepository.existsByTipoDocumentoIdAndNumeroDocumento(personaRequest.getTipoDocumento(),personaRequest.getNumeroDocumento())){
            throw new ResourceAlreadyExistsException("Ya existe una persona con ese tipo y número de documento");
        }

        ReniecResponseDto reniecResponseDto = null;
        //  DNI

        if (personaRequest.getTipoDocumento().equals(DatosConstant.DNI)){
            log.debug("Llamando a ws reniec");
            try {
                reniecResponseDto = reniecClient.getReniec(personaRequest.getNumeroDocumento(),apiToken);
            }catch (Exception e){
                throw new ExternalServiceException(e.getMessage());
            }
        }

        formatearDatosRequest(personaRequest);
        PersonaEntity personaEntity = modelMapper.map(personaRequest, PersonaEntity.class);
        personaEntity.setTipoDocumento(tipDocEnt);
        if (reniecResponseDto != null){
            personaEntity.setNombres(reniecResponseDto.getFirstname());
            personaEntity.setApellidoPaterno(reniecResponseDto.getFirstLastName());
            personaEntity.setApellidoMaterno(reniecResponseDto.getSecondLastName());
        }else{
            personaEntity.setNombres(personaRequest.getNombres());
            personaEntity.setApellidoPaterno(personaRequest.getApellidoPaterno());
            personaEntity.setApellidoMaterno(personaRequest.getApellidoMaterno());
        }

        personaEntity.setCreatedBy(securityUtils.getCurrentUserId());
        PersonaEntity saved = personaRepository.save(personaEntity);

        return modelMapper.map(saved, PersonaResponse.class);
    }

    @Transactional
    public PersonaDetailResponse updatePersona(UUID id, PersonaRequest personaRequest) {
        log.info("Iniciando persona request update");
        log.debug("Request : {}", personaRequest);

        //  procesamos dni
        TipoDocumentoEntity  tipDocEnt = tipoDocumentoRepository.findById(personaRequest.getTipoDocumento())
                .orElseThrow(()-> new ResourceNotFoundException("Tipo Documento no encontrado"));

        if(personaRepository.existsByCorreoAndIdNot(personaRequest.getCorreo(), id)){
            throw new ResourceAlreadyExistsException("El correo ingresado ya existe en otra persona");
        }

        if (personaRepository.existsByTipoDocumentoIdAndNumeroDocumentoAndIdNot(
                personaRequest.getTipoDocumento(),
                personaRequest.getNumeroDocumento(),
                id)){
            throw new ResourceAlreadyExistsException("Ya existe una persona con ese tipo y número de documento");
        }

        PersonaEntity personaFind = personaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Persona a actualizar no encontrada"));

        formatearDatosRequest(personaRequest);
        modelMapper.map(personaRequest, personaFind);
        personaFind.setTipoDocumento(tipDocEnt);
        personaFind.setUpdatedBy(securityUtils.getCurrentUserId());
        PersonaEntity saved = personaRepository.save(personaFind);
        return modelMapper.map(saved, PersonaDetailResponse.class);
    }

    @Transactional
    public void deletePersona(UUID id) {
        log.info("Iniciando persona request delete");
        log.debug("Request : {}", id);

        PersonaEntity personaFind = personaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Persona a eliminar no encontrada"));
        personaRepository.delete(personaFind);
    }

    @Transactional(readOnly = true)
    public PersonaDetailResponse findPersona(UUID id) {
        log.info("Iniciando persona request find");
        log.debug("Request : {}", id);

        PersonaEntity personaFind = personaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Persona buscada no encontrada"));
        return modelMapper.map(personaFind, PersonaDetailResponse.class);
    }

    public boolean existsPersonaPorDocumento(UUID tipoDocumento, String numeroDocumento){
        return personaRepository.existsByTipoDocumentoIdAndNumeroDocumento(tipoDocumento, numeroDocumento);
    }

    public PersonaDetailResponse findPersonaPorDocumento(UUID tipoDocumento, String numeroDocumento){
        PersonaEntity personaEntity = personaRepository.findByTipoDocumentoIdAndNumeroDocumento(tipoDocumento,numeroDocumento)
                .orElseThrow(()-> new ResourceNotFoundException("Persona no encontrada"));
        return modelMapper.map(personaEntity,PersonaDetailResponse.class);
    }


    private void formatearDatosRequest(PersonaRequest personaRequest){
        personaRequest.setNombres(personaRequest.getNombres().toUpperCase());
        personaRequest.setApellidoPaterno(personaRequest.getApellidoPaterno().toUpperCase());
        personaRequest.setApellidoMaterno(personaRequest.getApellidoMaterno().toUpperCase());
    }
}
