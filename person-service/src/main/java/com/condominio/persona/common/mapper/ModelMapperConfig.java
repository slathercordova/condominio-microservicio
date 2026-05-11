package com.condominio.persona.common.mapper;

import com.condominio.persona.persona.dto.response.PersonaDetailResponse;
import com.condominio.persona.persona.dto.response.PersonaResponse;
import com.condominio.persona.persona.entity.PersonaEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //  pasame de origen a destino por cada atributo, si los tipos y nombres
        //  de los atributos de las clases son iguales, no hay necesidad de mapear
        /*modelMapper.createTypeMap(TipoDocumentoRequest.class, TipoDocumentoEntity.class);
        modelMapper.createTypeMap(TipoDocumentoEntity.class, TipoDocumentoResponse.class);*/

        //  PersonaEntity -> PersonaDetailResponse
        modelMapper.typeMap(PersonaEntity.class, PersonaDetailResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getTipoDocumento().getId(),PersonaDetailResponse::setTipoDocumentoId);
            mapper.map(src -> src.getTipoDocumento().getNombre(),PersonaDetailResponse::setTipoDocumentoNombre);
        });

        //  PersonaEntity -> PersonaResponse
        modelMapper.typeMap(PersonaEntity.class, PersonaResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getTipoDocumento().getId(),PersonaResponse::setTipoDocumentoId);
            mapper.map(src -> src.getTipoDocumento().getNombre(),PersonaResponse::setTipoDocumentoNombre);
        });

        return modelMapper;
    }
}
