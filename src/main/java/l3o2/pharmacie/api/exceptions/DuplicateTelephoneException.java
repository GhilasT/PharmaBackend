package l3o2.pharmacie.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateTelephoneException extends RuntimeException {
    public DuplicateTelephoneException(String tel) {
        super("Le téléphone '" + tel + "' est déjà enregistré");
    }
}