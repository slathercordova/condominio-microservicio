package com.condominio.edificio.edificio.service;

import com.condominio.edificio.common.exception.BusinessException;
import com.condominio.edificio.common.exception.ResourceNotFoundException;
import com.condominio.edificio.edificio.dto.request.UnidadRequest;
import com.condominio.edificio.edificio.dto.response.UnidadDetailResponse;
import com.condominio.edificio.edificio.dto.response.UnidadResponse;
import com.condominio.edificio.edificio.entity.UnidadEntity;
import com.condominio.edificio.edificio.repository.EdificioRepository;
import com.condominio.edificio.edificio.repository.UnidadRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UnidadService {
    private final UnidadRepository unidadRepository;
    private final EdificioRepository edificioRepository;
    private final ModelMapper modelMapper;

    public UnidadService(UnidadRepository unidadRepository, EdificioRepository edificioRepository, ModelMapper modelMapper) {
        this.unidadRepository = unidadRepository;
        this.edificioRepository = edificioRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public UnidadResponse create(UnidadRequest unidadRequest) {
        //  validamos que el edificio exista
        if (!edificioRepository.existsById(unidadRequest.getIdEdificio())) {
            throw new ResourceNotFoundException("Edificio no encontrado");
        }

        //  guardamos la unidad
        UnidadEntity unidadEntity = modelMapper.map(unidadRequest, UnidadEntity.class);
        unidadEntity.setEstado(true);

        UnidadEntity unidadSaved = unidadRepository.save(unidadEntity);

        return modelMapper.map(unidadSaved, UnidadResponse.class);
    }

    @Transactional
    public Integer calcularPorcentajes(UUID idEdificio) {
        if (!edificioRepository.existsById(idEdificio)) {
            throw new ResourceNotFoundException("Edificio no encontrado");
        }

        //  actualizamos el metraje global de edificio con la suma total de todas las unidades...
        List<UnidadEntity> listaUnidades = unidadRepository.findByIdEdificioAndEstadoTrue(idEdificio);
        if (listaUnidades.isEmpty()) {
            throw new BusinessException("No hay unidades en el edificio que quiere calcular");
        }

        //  Calculamos metraje total
        BigDecimal metrajeTotal = BigDecimal.ZERO;

        for (UnidadEntity item : listaUnidades) {
            metrajeTotal = metrajeTotal.add(item.getMetraje());
        }

        if (metrajeTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El metraje total del edificio debe ser mayor a cero");
        }

        BigDecimal cien = BigDecimal.valueOf(100);

        //  Calculamos el % de participación de cada depa
        for (UnidadEntity item : listaUnidades) {
            BigDecimal porcentajeTmp = (item.getMetraje()
                    .divide(metrajeTotal, 6, RoundingMode.HALF_UP))
                    .multiply(cien)
                    .setScale(3, RoundingMode.HALF_UP);
            item.setPorcentaje(porcentajeTmp);
        }
        unidadRepository.saveAll(listaUnidades);
        return listaUnidades.size();
    }

    public UnidadDetailResponse detailUnidad(UUID id){
        if (id==null){
            throw new BusinessException("No ingresó el ID de la unidad");
        }

        UnidadEntity unidad = unidadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Unidad no encontrada"));

        return modelMapper.map(unidad, UnidadDetailResponse.class);
    }
}
