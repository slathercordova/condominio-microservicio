package com.condominio.persona.mapper;

import com.condominio.persona.dto.request.TipoDocumentoRequest;
import com.condominio.persona.dto.response.TipoDocumentoResponse;
import com.condominio.persona.entity.TipoDocumentoEntity;
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

        //  pasame de origen a destino por cada atributo
        modelMapper.createTypeMap(TipoDocumentoRequest.class, TipoDocumentoEntity.class);
        modelMapper.createTypeMap(TipoDocumentoEntity.class, TipoDocumentoResponse.class);

        return modelMapper;
    }
}
