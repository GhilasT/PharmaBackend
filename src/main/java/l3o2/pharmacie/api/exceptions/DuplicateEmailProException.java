package l3o2.pharmacie.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'un email professionnel est déjà utilisé.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailProException extends RuntimeException {
    /**
     * Construit une nouvelle exception DuplicateEmailProException.
     *
     * @param emailPro L'email professionnel dupliqué.
     */
    public DuplicateEmailProException(String emailPro) {
        super("Email professionnel déjà utilisé : " + emailPro);
    }
}