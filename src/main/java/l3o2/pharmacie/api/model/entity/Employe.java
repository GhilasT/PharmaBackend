package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe représentant un employé de la pharmacie (Administrateur, Pharmacien Adjoint, Préparateur, etc.).
 * Hérite de la classe Personne et ajoute des attributs spécifiques aux employés.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Employe extends Personne implements UserDetails {
    @Column(nullable = false, unique = true, updatable = false)
    /** Matricule unique de l'employé. */
    private String matricule;

    @Column(nullable = false)
    /** Date d'embauche de l'employé. */
    private Date dateEmbauche;

    @Column(nullable = false)
    /** Salaire de l'employé. */
    private Double salaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    /** Poste occupé par l'employé. */
    private PosteEmploye poste;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    /** Statut du contrat de l'employé. */
    private StatutContrat statutContrat;

    @Column
    /** Diplôme(s) de l'employé. */
    private String diplome;

    @Column(nullable = false)
    /** Mot de passe de l'employé (haché). */
    private String password;

    @Column(nullable = false, unique = true)
    /** Adresse email professionnelle de l'employé, utilisée comme nom d'utilisateur. */
    private String emailPro;

    @ElementCollection(fetch = FetchType.EAGER) // Permet de stocker plusieurs rôles
    /** Liste des permissions (rôles) de l'employé. */
    private List<String> permissions;

    /**
     * Retourne les autorités accordées à l'utilisateur.
     * @return Une collection d'objets {@link GrantedAuthority} représentant les rôles de l'employé.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    /**
     * Retourne le nom d'utilisateur utilisé pour l'authentification.
     * Dans ce cas, c'est l'adresse email professionnelle.
     * @return L'adresse email professionnelle de l'employé.
     */
    @Override
    public String getUsername() {
        return emailPro;
    }

    /**
     * Hache et crypte le mot de passe avant l'enregistrement en base de données.
     * Cette méthode est appelée automatiquement avant la persistance de l'entité
     * si le mot de passe n'est pas déjà haché (ne commence pas par "$2a$").
     */
    @PrePersist
    private void hashPassword() {
        if (this.password != null && !this.password.startsWith("$2a$")) { // Vérifie le préfixe BCrypt
            this.password = new BCryptPasswordEncoder().encode(this.password);
        }
    }

    /**
     * Génère un matricule pour chaque employé en fonction de son poste.
     * Le matricule est composé d'un préfixe "EMP-", du nom du poste en majuscules,
     * et d'un numéro unique à 5 chiffres (ex: EMP-ADMIN-00001).
     * @param baseMatricule La base du matricule, généralement le nom du poste.
     */
    public void generateMatricule(String baseMatricule) {
        String prefix = "EMP-" + baseMatricule.toUpperCase() + "-";
        String suffix = String.format("%05d", (int) (Math.random() * 100000));
        this.matricule = prefix + suffix;
    }

}