package org.example.privatbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for handling exceptions across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions thrown when method arguments fail validation.
     *
     * @param ex the MethodArgumentNotValidException
     * @return ResponseEntity containing field error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        // Map to hold field names and error messages
        Map<String, String> errors = new HashMap<>();
        // Populate the map with field errors
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        // Return a bad request response with the errors
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles runtime exceptions such as custom exceptions.
     *
     * @param ex      the RuntimeException
     * @param request the WebRequest
     * @return ResponseEntity containing the exception message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        // Return a bad request response with the exception message
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
