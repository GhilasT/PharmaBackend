package l3o2.pharmacie.api.handler;

import l3o2.pharmacie.api.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Format standard des réponses d'erreur
    public record ErrorResponse(
        LocalDateTime timestamp,
        int statusCode,
        String error,
        String message,
        String path
    ) {}

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Ressource introuvable",
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler({
        DuplicateEmailProException.class,
        DuplicateEmailException.class,
        DuplicateRPPSException.class,
        DuplicateTelephoneException.class,
        DuplicateSocieteException.class
    })
    
    public ResponseEntity<ErrorResponse> handleDuplicateDataExceptions(
            RuntimeException ex, WebRequest request) {
        return buildErrorResponse(
            HttpStatus.CONFLICT,
            "Conflit de données",
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler({InvalidDataException.class, InvalidOperationException.class})
    public ResponseEntity<ErrorResponse> handleInvalidDataExceptions(
            RuntimeException ex, WebRequest request) {
        return buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Données invalides",
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {
        return buildErrorResponse(
            HttpStatus.CONFLICT,
            "Violation de contraintes base de données",
            ex.getMostSpecificCause().getMessage(),
            request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        return buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erreur interne du serveur",
            "Une erreur inattendue s'est produite",
            request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String errorType,
            String message,
            WebRequest request) {
        
        return new ResponseEntity<>(
            new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                errorType,
                message,
                request.getDescription(false).replace("uri=", "")
            ),
            status
        );
    }
}