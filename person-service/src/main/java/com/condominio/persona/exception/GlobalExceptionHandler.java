package com.condominio.persona.exception;

import com.condominio.persona.dto.response.ApiResponse;
import com.condominio.persona.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
    ResponseEntity = es una clase de spring, no lo hemos creado nosotros
    */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(ValidationException ex) {
        log.error("Error ValidationException: ", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), ErrorCode.VALIDATION_ERROR, null));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex){
        log.error("Error ResourceNotFoundException: ", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false,ex.getMessage(), ErrorCode.RESOURCE_NOT_FOUND_ERROR, null));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiResponse<Void>> handleExternalServiceException(ExternalServiceException ex){
        log.error("Error ExternalServiceException: ", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ApiResponse<>(false,ex.getMessage(), ErrorCode.EXTERNAL_RESOURCE_ERROR, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex){
        log.error("Error Exception: ", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, ex.getMessage(), ErrorCode.GENERAL_ERROR, null));
    }

    //  Metodo para recuperar las validaciones de notaciones de spring, ejemplo clase cualquier DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, List<String>> mapErrores = new HashMap<>();
        List<FieldError> listaErrores = ex.getBindingResult().getFieldErrors();
        for (FieldError error : listaErrores){
            /*
            computeIfAbsent buscar por clave y si la clave no existe entonces la crea, si existe la usa
            */
            mapErrores.computeIfAbsent(error.getField(), k-> new ArrayList<>()).add(error.getDefaultMessage());
        }
        log.error("Error MethodArgumentNotValidException: ", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false,"VALIDATION_ERROR", ErrorCode.VALIDATION_ERROR,mapErrores));
    }

    // 🔹 4. DB (UNIQUE, FK, etc)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Error de integridad de datos";
        Throwable root = ex.getRootCause();

        if (root instanceof PSQLException psqlEx) {
            String constraint = psqlEx.getServerErrorMessage().getConstraint();

            String sqlState = psqlEx.getSQLState();

            // 🔥 LOG INTERNO (AQUÍ ESTÁ LA CLAVE)
            System.err.println("Código sql: " + sqlState + " Constraint violado: " + constraint);
            log.error("Error DataIntegrityViolationException: ", constraint, ex);

            switch (sqlState) {
                case "23505":
                    message = "Registro duplicado (violación UNIQUE)";
                    break;
                case "23503":
                    message = "Referencia inválida (FK)";
                    break;
                case "23502":
                    message = "Campo obligatorio faltante";
                    break;
                default:
                    message = "Error de base de datos";
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, message, ErrorCode.DATA_INTEGRITY_ERROR, null));
    }

}
