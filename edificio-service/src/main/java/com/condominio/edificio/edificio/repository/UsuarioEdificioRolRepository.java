package com.condominio.edificio.edificio.repository;

import com.condominio.edificio.edificio.dto.response.RolResponse;
import com.condominio.edificio.edificio.entity.UsuarioEdificioRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UsuarioEdificioRolRepository extends JpaRepository<UsuarioEdificioRolEntity, UUID>, JpaSpecificationExecutor<UsuarioEdificioRolEntity> {
    boolean existsByIdUsuarioAndIdEdificioAndIdRol(UUID idUsuario, UUID idEdificio, UUID idRol);

    @Query("""
                select new com.condominio.edificio.edificio.dto.response.RolResponse(r.id,  r.nombre, r.estado)
                from UsuarioEdificioRolEntity uer
                join RolEntity r on r.id = uer.idRol
                where uer.idUsuario = :idUsuario
                  and uer.idEdificio = :idEdificio
                  and uer.estado = true
                  and r.estado = true
            """)
    List<RolResponse> findRolesByUsuarioAndEdificio(UUID idUsuario, UUID idEdificio);
}
