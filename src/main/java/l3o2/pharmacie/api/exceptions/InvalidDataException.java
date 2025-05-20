package l3o2.pharmacie.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsque les données fournies sont invalides ou corrompues.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDataException extends RuntimeException {
    /**
     * Construit une nouvelle exception InvalidDataException.
     *
     * @param message Message d'erreur décrivant l'invalidité des données.
     */
    public InvalidDataException(String message) {
        super(message);
    }
}