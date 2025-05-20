package l3o2.pharmacie.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'une opération demandée est invalide dans le contexte actuel.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOperationException extends RuntimeException {
    /**
     * Construit une nouvelle exception InvalidOperationException.
     *
     * @param message Message d'erreur décrivant pourquoi l'opération est invalide.
     */
    public InvalidOperationException(String message) {
        super(message);
    }
}