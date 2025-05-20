package l3o2.pharmacie.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'une adresse email est déjà utilisée.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {
    /**
     * Construit une nouvelle exception DuplicateEmailException.
     *
     * @param email L'adresse email dupliquée.
     */
    public DuplicateEmailException(String email) {
        super("L'email '" + email + "' est déjà utilisé");
    }
}
