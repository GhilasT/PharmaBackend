package l3o2.pharmacie.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'un numéro de téléphone est déjà enregistré dans le système.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateTelephoneException extends RuntimeException {
    /**
     * Construit une nouvelle exception DuplicateTelephoneException.
     *
     * @param tel Le numéro de téléphone dupliqué.
     */
    public DuplicateTelephoneException(String tel) {
        super("Le téléphone '" + tel + "' est déjà enregistré");
    }
}