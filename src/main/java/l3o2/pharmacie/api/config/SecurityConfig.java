package l3o2.pharmacie.api.config;

import l3o2.pharmacie.util.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration sécurité de l'application
 * @author raphaelcharoze
 */
@EnableMethodSecurity(jsr250Enabled = true)
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Aucune page n'est accessible sans être authentifié sauf login
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/auth/login").permitAll()  // Public routes
                                .requestMatchers("/api/auth/register/**").hasAnyRole("ADMINISTRER")
                                .requestMatchers("/api/administrateurs/**").hasAnyRole("GERER_ADMIN") //seulement les titulaires peuvent accéder aux administrateurs
                                .requestMatchers("/api/apprentis/**").hasAnyRole("ADMINISTRER")
                                .requestMatchers("/api/client/**").hasAnyRole("VENDRE") //seulement les vendeurs peuvent accéder aux clients
                                .requestMatchers("/api/employes/**").hasAnyRole("ADMINISTRER")
                                .requestMatchers("/api/fournisseurs/**").hasAnyRole("COMMANDER") //seulement les commandeurs peuvent accéder aux fournisseurs
                                .requestMatchers("/api/medecins/**").hasAnyRole("VENDRE") //seulement les vendeurs peuvent accéder aux médecins
                                .requestMatchers("/api/medicaments/**").hasAnyRole("VENDRE", "COMMANDER") //seulement les vendeurs et les commandeurs peuvent accéder aux médicaments
                                .requestMatchers("/api/ordonnances/**").hasAnyRole("VENDRE") //seulement les vendeurs peuvent accéder aux ordonnances
                                .requestMatchers("/api/pharmaciens-adjoints/**").hasAnyRole("ADMINISTRER")
                                .requestMatchers("/api/preparateurs/**").hasAnyRole("ADMINISTRER")
                                .requestMatchers("/api/titulaires/**").hasAnyRole("GERER_ADMIN") //seulement les titulaires peuvent accéder aux titulaires
                                .requestMatchers("/api/ventes/**").hasAnyRole("VENDRE") //seulement les vendeurs peuvent accéder aux ventes
                                //.requestMatchers("api/email/send").hasAnyRole("ADMINISTRER")
                                .anyRequest().authenticated())            // Protected routes
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuration de l'encodeur de mdp
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
