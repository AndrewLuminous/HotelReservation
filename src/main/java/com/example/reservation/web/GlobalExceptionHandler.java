package com.example.reservation.web;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex)
    {
        log.error(ex.getMessage(), ex);

        var errorDto = new ErrorResponseDto(
                "Internal server error",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(errorDto);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(EntityNotFoundException ex)
    {
        log.error(ex.getMessage(), ex);

        var errorDto = new ErrorResponseDto(
                "Entity not found",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(errorDto);
    }
    @ExceptionHandler(exception = {IllegalArgumentException.class, IllegalStateException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> handleBadRequest(Exception ex)
    {
        log.error(ex.getMessage(), ex);

        var errorDto = new ErrorResponseDto(
                "Bad request",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(errorDto);
    }
}
