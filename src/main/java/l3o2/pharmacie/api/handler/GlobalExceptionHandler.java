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

/**
 * Gestionnaire global des exceptions pour l'application.
 * Intercepte les exceptions spécifiques et les exceptions générales pour fournir des réponses d'erreur standardisées.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Représente le format standard d'une réponse d'erreur.
     * @param timestamp L'horodatage de l'erreur.
     * @param statusCode Le code de statut HTTP.
     * @param error Le type d'erreur.
     * @param message Le message détaillé de l'erreur.
     * @param path Le chemin de la requête qui a causé l'erreur.
     */
    public record ErrorResponse(
        LocalDateTime timestamp,
        int statusCode,
        String error,
        String message,
        String path
    ) {}

    /**
     * Gère les exceptions de type {@link ResourceNotFoundException}.
     * @param ex L'exception interceptée.
     * @param request La requête web en cours.
     * @return Une {@link ResponseEntity} contenant les détails de l'erreur et le statut HTTP NOT_FOUND.
     */
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

    /**
     * Gère les exceptions liées aux données dupliquées.
     * Cela inclut {@link DuplicateEmailProException}, {@link DuplicateEmailException},
     * {@link DuplicateRPPSException}, {@link DuplicateTelephoneException}, et {@link DuplicateSocieteException}.
     * @param ex L'exception d'exécution interceptée.
     * @param request La requête web en cours.
     * @return Une {@link ResponseEntity} contenant les détails de l'erreur et le statut HTTP CONFLICT.
     */
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

    /**
     * Gère les exceptions liées aux données invalides ou aux opérations invalides.
     * Cela inclut {@link InvalidDataException} et {@link InvalidOperationException}.
     * @param ex L'exception d'exécution interceptée.
     * @param request La requête web en cours.
     * @return Une {@link ResponseEntity} contenant les détails de l'erreur et le statut HTTP BAD_REQUEST.
     */
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

    /**
     * Gère les exceptions de validation des arguments de méthode (par exemple, annotations de validation JSR 380).
     * @param ex L'exception {@link MethodArgumentNotValidException} interceptée.
     * @return Une {@link ResponseEntity} contenant une map des champs et leurs messages d'erreur, et le statut HTTP BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les exceptions de violation d'intégrité des données (par exemple, contraintes de base de données).
     * @param ex L'exception {@link DataIntegrityViolationException} interceptée.
     * @param request La requête web en cours.
     * @return Une {@link ResponseEntity} contenant les détails de l'erreur et le statut HTTP CONFLICT.
     */
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

    /**
     * Gère toutes les autres exceptions non interceptées par des gestionnaires spécifiques.
     * @param ex L'exception générale interceptée.
     * @param request La requête web en cours.
     * @return Une {@link ResponseEntity} contenant un message d'erreur générique et le statut HTTP INTERNAL_SERVER_ERROR.
     */
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

    /**
     * Construit une {@link ResponseEntity} standardisée pour les erreurs.
     * @param status Le statut HTTP de la réponse.
     * @param errorType Le type d'erreur (par exemple, "Ressource introuvable").
     * @param message Le message détaillé de l'erreur.
     * @param request La requête web en cours, utilisée pour extraire le chemin.
     * @return Une {@link ResponseEntity} contenant l'objet {@link ErrorResponse} et le statut HTTP.
     */
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