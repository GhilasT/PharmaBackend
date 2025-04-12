package l3o2.pharmacie.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailProException extends RuntimeException {
    public DuplicateEmailProException(String emailPro) {
        super("Email professionnel déjà utilisé : " + emailPro);
    }
}