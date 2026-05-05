package com.condominio.persona.repository;

import com.condominio.persona.entity.TipoDocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumentoEntity, UUID> {
}
