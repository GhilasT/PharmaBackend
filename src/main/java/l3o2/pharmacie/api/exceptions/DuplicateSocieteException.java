package l3o2.pharmacie.api.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateSocieteException extends RuntimeException {
    public DuplicateSocieteException(String societe) {
        super("La société '" + societe + "' est déjà utilisée");
    }
}