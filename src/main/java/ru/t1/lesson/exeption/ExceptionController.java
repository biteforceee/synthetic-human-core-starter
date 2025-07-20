package ru.t1.lesson.exeption;


import org.apache.kafka.common.protocol.types.Field;
import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(QueueIsFullException.class)
    public ResponseEntity<String> handleQueueIsFull(QueueIsFullException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Очередь полностью заполнена " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ошибка " + e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMediaType(HttpMessageNotReadableException e) {
        Throwable rootCause = e.getRootCause();

        if (rootCause instanceof IllegalArgumentException && rootCause.getMessage().contains("No enum constant")) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "error", "Неверное значение ENUM",
                            "allowedValues", Arrays.toString(OperationType.values())
                    ));
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of("error", "Неверный формат JSON"));
    }
}
