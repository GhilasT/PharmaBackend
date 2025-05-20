package l3o2.pharmacie.api.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Exception levée lorsqu'un nom de société est déjà utilisé.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateSocieteException extends RuntimeException {
    /**
     * Construit une nouvelle exception DuplicateSocieteException.
     *
     * @param societe Le nom de la société dupliqué.
     */
    public DuplicateSocieteException(String societe) {
        super("La société '" + societe + "' est déjà utilisée");
    }
}