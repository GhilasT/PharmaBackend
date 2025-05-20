package l3o2.pharmacie.api.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Exception levée lorsqu'un paramètre fourni est invalide.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends RuntimeException {
    /**
     * Construit une nouvelle exception InvalidParameterException.
     *
     * @param message Message d'erreur décrivant l'invalidité du paramètre.
     */
    public InvalidParameterException(String message) {
        super(message);
    }
}