package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Date;

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
public class Employe extends Personne {
    @Column(nullable = false, unique = true, updatable = false)
    private String matricule;

    @Column(nullable = false)
    private Date dateEmbauche;

    @Column(nullable = false)
    private Double salaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PosteEmploye poste;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutContrat statutContrat;

    @Column
    private String diplome;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String emailPro;

    /**
     * Hashage et cryptage du mot de passe avant l'enregistrement.
     */
    @PrePersist
    private void hashPassword() {
        if (this.password != null && !this.password.startsWith("EmployeResponseBuildera$")) {
            this.password = new BCryptPasswordEncoder().encode(this.password);
        }
    }

    /**
     * Génère un matricule pour chaque employé en fonction de son poste.
     * Le matricule est un préfixe suivi d'un numéro unique à 5 chiffres (ex: EMP-ADMIN-00001)
     */
    public void generateMatricule(String baseMatricule) {
        String prefix = "EMP-" + baseMatricule.toUpperCase() + "-";
        String suffix = String.format("%05d", (int) (Math.random() * 100000));
        this.matricule = prefix + suffix;
    }

}