package com.himfg.permission.utils;

import com.himfg.permission.dto.response.Base.BaseResponse;
import com.himfg.permission.dto.response.Base.Message;
import com.himfg.permission.dto.response.Base.Response;
import com.himfg.permission.exceptions.DuplicateEntryException;
import com.himfg.permission.exceptions.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<?> noData(NoDataFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<?> alreadyExists(DuplicateEntryException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> all(Exception ex) {
        log.error("Unhandled exception", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        log.error("Error en la validación", ex);
        return build(HttpStatus.BAD_REQUEST, message, ex);
    }

    private ResponseEntity<BaseResponse<?>> build(HttpStatus status, String message, Exception ex) {
        return ResponseEntity.status(status)
                .body(new BaseResponse<>(
                        status.value(),
                        new Message<>(
                                null,
                                new Response<>(message, ex)
                        )
                ));
    }
}
