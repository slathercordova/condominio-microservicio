package com.condominio.persona.persona.service;

import com.condominio.persona.common.exception.ExternalServiceException;
import com.condominio.persona.common.exception.ResourceAlreadyExistsException;
import com.condominio.persona.common.exception.ResourceNotFoundException;
import com.condominio.persona.common.pagination.PaginatedResponse;
import com.condominio.persona.common.pagination.Pagination;
import com.condominio.persona.common.util.DatosConstant;
import com.condominio.persona.common.util.SecurityUtils;
import com.condominio.persona.feignClient.ReniecClient;
import com.condominio.persona.feignClient.ReniecResponseDto;
import com.condominio.persona.persona.dto.filter.PersonaFilter;
import com.condominio.persona.persona.dto.request.PersonaRequest;
import com.condominio.persona.persona.dto.response.PersonaDetailResponse;
import com.condominio.persona.persona.dto.response.PersonaResponse;
import com.condominio.persona.persona.entity.PersonaEntity;
import com.condominio.persona.persona.repository.PersonaRepository;
import com.condominio.persona.persona.specification.PersonaSpecification;
import com.condominio.persona.tipodocumento.entity.TipoDocumentoEntity;
import com.condominio.persona.tipodocumento.repository.TipoDocumentoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        if (personaRepository.existsByCorreo(personaRequest.getCorreo())) {
            throw new ResourceAlreadyExistsException("El correo ingresado ya existe");
        }

        //  procesamos dni
        TipoDocumentoEntity tipDocEnt = tipoDocumentoRepository.findById(personaRequest.getTipoDocumento())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo Documento no encontrado"));

        if (personaRepository.existsByTipoDocumentoIdAndNumeroDocumento(personaRequest.getTipoDocumento(), personaRequest.getNumeroDocumento())) {
            throw new ResourceAlreadyExistsException("Ya existe una persona con ese tipo y número de documento");
        }

        ReniecResponseDto reniecResponseDto = null;
        //  DNI

        if (personaRequest.getTipoDocumento().equals(DatosConstant.DNI)) {
            log.debug("Llamando a ws reniec");
            try {
                reniecResponseDto = reniecClient.getReniec(personaRequest.getNumeroDocumento(), apiToken);
                personaRequest.setNombres(reniecResponseDto.getFirstname());
                personaRequest.setApellidoPaterno(reniecResponseDto.getFirstLastName());
                personaRequest.setApellidoMaterno(reniecResponseDto.getSecondLastName());
            } catch (Exception e) {
                throw new ExternalServiceException(e.getMessage());
            }
        }

        formatearDatosRequest(personaRequest);
        PersonaEntity personaEntity = modelMapper.map(personaRequest, PersonaEntity.class);
        personaEntity.setTipoDocumento(tipDocEnt);
        if (reniecResponseDto != null) {
            personaEntity.setNombres(reniecResponseDto.getFirstname());
            personaEntity.setApellidoPaterno(reniecResponseDto.getFirstLastName());
            personaEntity.setApellidoMaterno(reniecResponseDto.getSecondLastName());
        } else {
            personaEntity.setNombres(personaRequest.getNombres());
            personaEntity.setApellidoPaterno(personaRequest.getApellidoPaterno());
            personaEntity.setApellidoMaterno(personaRequest.getApellidoMaterno());
        }

        PersonaEntity saved = personaRepository.save(personaEntity);

        return modelMapper.map(saved, PersonaResponse.class);
    }

    @Transactional
    public PersonaDetailResponse updatePersona(UUID id, PersonaRequest personaRequest) {
        log.info("Iniciando persona request update");
        log.debug("Request : {}", personaRequest);

        //  procesamos dni
        TipoDocumentoEntity tipDocEnt = tipoDocumentoRepository.findById(personaRequest.getTipoDocumento())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo Documento no encontrado"));

        if (personaRepository.existsByCorreoAndIdNot(personaRequest.getCorreo(), id)) {
            throw new ResourceAlreadyExistsException("El correo ingresado ya existe en otra persona");
        }

        if (personaRepository.existsByTipoDocumentoIdAndNumeroDocumentoAndIdNot(
                personaRequest.getTipoDocumento(),
                personaRequest.getNumeroDocumento(),
                id)) {
            throw new ResourceAlreadyExistsException("Ya existe una persona con ese tipo y número de documento");
        }

        PersonaEntity personaFind = personaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Persona a actualizar no encontrada"));

        formatearDatosRequest(personaRequest);
        modelMapper.map(personaRequest, personaFind);
        personaFind.setTipoDocumento(tipDocEnt);
        PersonaEntity saved = personaRepository.save(personaFind);
        return modelMapper.map(saved, PersonaDetailResponse.class);
    }

    @Transactional
    public void deletePersona(UUID id) {
        log.info("Iniciando persona request delete");
        log.debug("Request : {}", id);

        PersonaEntity personaFind = personaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Persona a eliminar no encontrada"));
        personaRepository.delete(personaFind);
    }

    @Transactional(readOnly = true)
    public PersonaDetailResponse findPersona(UUID id) {
        log.info("Iniciando persona request find");
        log.debug("Request : {}", id);

        PersonaEntity personaFind = personaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Persona buscada no encontrada"));
        return modelMapper.map(personaFind, PersonaDetailResponse.class);
    }

    @Transactional(readOnly = true)
    public List<PersonaDetailResponse> findAllPersona() {
        List<PersonaDetailResponse> listaResponse = personaRepository.findAll(Sort.by(Sort.Direction.ASC, "apellidoPaterno"))
                .stream()
                .map(item -> modelMapper.map(item, PersonaDetailResponse.class))
                .toList();
        return listaResponse;
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<PersonaDetailResponse> findByFilters(
            PersonaFilter filter,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<PersonaEntity> specification = PersonaSpecification.byFilters(filter);
        Page<PersonaDetailResponse> result = personaRepository.findAll(specification, pageable).map(this::toResponse);

        Pagination pagination = new Pagination(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast(),
                result.hasNext(),
                result.hasPrevious()
        );
        return new PaginatedResponse<>(result.getContent(),pagination);
    }

    public boolean existsPersonaPorDocumento(UUID tipoDocumento, String numeroDocumento) {
        return personaRepository.existsByTipoDocumentoIdAndNumeroDocumento(tipoDocumento, numeroDocumento);
    }

    public PersonaDetailResponse findPersonaPorDocumento(UUID tipoDocumento, String numeroDocumento) {
        PersonaEntity personaEntity = personaRepository.findByTipoDocumentoIdAndNumeroDocumento(tipoDocumento, numeroDocumento)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada"));
        return modelMapper.map(personaEntity, PersonaDetailResponse.class);
    }


    private void formatearDatosRequest(PersonaRequest personaRequest) {
        personaRequest.setNombres(personaRequest.getNombres().toUpperCase());
        personaRequest.setApellidoPaterno(personaRequest.getApellidoPaterno().toUpperCase());
        personaRequest.setApellidoMaterno(personaRequest.getApellidoMaterno().toUpperCase());
    }

    private PersonaDetailResponse toResponse(PersonaEntity entity) {
        return modelMapper.map(entity, PersonaDetailResponse.class);
    }
}
