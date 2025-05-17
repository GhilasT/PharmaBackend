package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.LoginRequest;
import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service pour l'authentification
 * @date 2025-03-21 (dernière modification)
 * @author raphaelcharoze
 */
@Service
@AllArgsConstructor
public class AuthService implements AuthenticationProvider {
    private EmployeService employeService;
    private final JwtUtil jwtUtil;

    /**
     * Authentification d'un utilisateur : verifie que le mdp et l'utilisateur matchent
     * @return Employe
     */
    public String authenticate(LoginRequest request) {
        try {
            Employe user = employeService.getEmployeByEmailPro(request.getEmail());
            if (!(new BCryptPasswordEncoder()).matches(request.getPassword(), user.getPassword())) {
                return null;
            }else{
                return jwtUtil.generateToken(user.getIdPersonne(), user.getAuthorities());
            }
        } catch (ResponseStatusException e) {
            return null;
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        String token = authenticate(LoginRequest.builder().password(password).email(email).build());

        if(token != null) {
            Employe user = employeService.getEmployeByEmailPro(email);
            return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        }
        return null;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}