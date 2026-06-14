package com.condominio.persona.tipodocumento;
import com.condominio.persona.common.exception.ResourceAlreadyExistsException;
import com.condominio.persona.common.exception.ResourceNotFoundException;
import com.condominio.persona.tipodocumento.dto.request.TipoDocumentoRequest;
import com.condominio.persona.tipodocumento.entity.TipoDocumentoEntity;
import com.condominio.persona.tipodocumento.repository.TipoDocumentoRepository;
import com.condominio.persona.tipodocumento.service.TipoDocumentoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoDocumentoServiceTest {

    @InjectMocks
    private TipoDocumentoService tipoDocumentoService;

    @Mock
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Test
    void deleteTipoDocumento_happyPath() {

        UUID id = UUID.randomUUID();

        TipoDocumentoEntity entity = new TipoDocumentoEntity();
        entity.setId(id);

        when(tipoDocumentoRepository.findById(id))
                .thenReturn(Optional.of(entity));

        doNothing().when(tipoDocumentoRepository).delete(entity);

        assertDoesNotThrow(() -> tipoDocumentoService.deleteTipoDocumento(id));

        verify(tipoDocumentoRepository).delete(entity);
    }

    @Test
    void deleteTipoDocumento_notFound() {

        UUID id = UUID.randomUUID();

        when(tipoDocumentoRepository.findById(id))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> tipoDocumentoService.deleteTipoDocumento(id)
        );

        assertEquals("Id no encontrado", ex.getMessage());
    }

    @Test
    void createTipoDocumento_yaExiste() {

        TipoDocumentoRequest request = new TipoDocumentoRequest();
        request.setNombre("Documento nacional de Identidad");
        request.setNombreCorto("DNI");

        when(tipoDocumentoRepository.existsByNombre(anyString()))
                .thenReturn(true);

        ResourceAlreadyExistsException ex = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> tipoDocumentoService.createTipoDocumento(request)
        );

        assertEquals("Ya existe un tipo de documento con ese nombre", ex.getMessage());

        verify(tipoDocumentoRepository, never()).save(any());
    }
}