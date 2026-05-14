package com.condominio.auth.common.mapper;

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

        return modelMapper;
    }
}
