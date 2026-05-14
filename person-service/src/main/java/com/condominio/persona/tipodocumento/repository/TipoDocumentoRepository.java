package com.condominio.persona.tipodocumento.repository;

import com.condominio.persona.tipodocumento.entity.TipoDocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumentoEntity, UUID>, JpaSpecificationExecutor<TipoDocumentoEntity> {
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String correo, UUID id);
}
