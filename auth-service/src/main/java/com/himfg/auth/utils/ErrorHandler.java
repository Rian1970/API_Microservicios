package com.himfg.auth.utils;

import com.himfg.auth.dto.response.Base.BaseResponse;
import com.himfg.auth.dto.response.Base.Message;
import com.himfg.auth.dto.response.Base.Response;
import com.himfg.auth.exceptions.DuplicateEntryException;
import com.himfg.auth.exceptions.ForbiddenException;
import com.himfg.auth.exceptions.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> forbidedn(ForbiddenException ex){
        log.error("Forbidden ", ex);
        return build(HttpStatus.FORBIDDEN, "Usuario sin autorización", ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos.", ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> all(Exception ex) {
        log.error("Unhandled exception", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
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
