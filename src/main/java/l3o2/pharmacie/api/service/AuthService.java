package l3o2.pharmacie.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import l3o2.pharmacie.api.repository.PersonneRepository;

@Service
public class AuthService {
    @Autowired
    private PersonneRepository personneRepository;

    public boolean authenticate(String email, String password) {
        return personneRepository.findByEmailAndPassword(email, password).isPresent();
    }
}