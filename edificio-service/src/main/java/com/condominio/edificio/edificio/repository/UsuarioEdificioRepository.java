package com.condominio.edificio.edificio.repository;

import com.condominio.edificio.edificio.dto.response.ListaEdificiosXUsuarioResponse;
import com.condominio.edificio.edificio.entity.UsuarioEdificioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UsuarioEdificioRepository extends JpaRepository<UsuarioEdificioEntity, UUID>, JpaSpecificationExecutor<UsuarioEdificioEntity> {
    @Query("""
            select new com.condominio.edificio.edificio.dto.response.ListaEdificiosXUsuarioResponse(edi.id, edi.nombre)
                            from UsuarioEdificioEntity ue
                            join EdificioEntity edi on ue.idEdificio = edi.id
                            where ue.idUsuario = :idUsuario
                              and ue.estado = true
                              and edi.estado = true
            """)
    List<ListaEdificiosXUsuarioResponse> listaEdificiosPorUsuario(@Param("idUsuario") UUID idUsuario);

    boolean existsByIdUsuarioAndIdEdificioAndEstadoTrue(UUID idUsuario, UUID idEdificio);
}
