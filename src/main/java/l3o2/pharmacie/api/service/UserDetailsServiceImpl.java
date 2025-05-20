package l3o2.pharmacie.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import l3o2.pharmacie.api.repository.EmployeRepository;

/**
 * Implémentation de {@link UserDetailsService} pour charger les détails de l'utilisateur
 * à partir du référentiel des employés. Utilisé par Spring Security pour l'authentification.
 */
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeRepository employeRepository;
    
    /**
     * Charge les détails de l'utilisateur par son nom d'utilisateur (email professionnel).
     * @param username Le nom d'utilisateur (email professionnel) de l'employé à rechercher.
     * @return Un objet {@link UserDetails} contenant les informations de l'utilisateur.
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé avec le nom d'utilisateur fourni.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeRepository.findByEmailPro(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
