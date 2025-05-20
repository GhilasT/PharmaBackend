package l3o2.pharmacie.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'un numéro RPPS est déjà utilisé.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateRPPSException extends RuntimeException {
    /**
     * Construit une nouvelle exception DuplicateRPPSException.
     *
     * @param rpps Le numéro RPPS dupliqué.
     */
    public DuplicateRPPSException(String rpps) {
        super("Le numéro RPPS '" + rpps + "' est déjà utilisé");
    }
}