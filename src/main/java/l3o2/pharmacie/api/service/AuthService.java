package l3o2.pharmacie.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.api.repository.EmployeRepository;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private EmployeRepository employeRepository;

    public Optional<Employe> authenticate(String emailPro, String password) {
        Optional<Employe> employeOpt = employeRepository.findByEmailPro(emailPro);
        if (employeOpt.isPresent()) {
            Employe employe = employeOpt.get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, employe.getPassword())) {
                return Optional.of(employe);
            }
        }
        return Optional.empty();
    }
}