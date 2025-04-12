package l3o2.pharmacie.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateRPPSException extends RuntimeException {
    public DuplicateRPPSException(String rpps) {
        super("Le numéro RPPS '" + rpps + "' est déjà utilisé");
    }
}