package l3o2.pharmacie.api.model.dto.request;

import l3o2.pharmacie.api.model.entity.Fournisseur;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO pour la création d'une commande auprès d'un fournisseur.
 * Contient les informations nécessaires pour enregistrer une nouvelle commande.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeCreateRequest {

    /**
     * L'identifiant unique du fournisseur auprès duquel la commande est passée.
     */
    private UUID fournisseurId;
    /**
     * L'identifiant unique du pharmacien adjoint qui a créé la commande.
     */
    private UUID pharmacienAdjointId;
    /**
     * La liste des lignes de commande, détaillant les médicaments et quantités commandés.
     */
    private List<LigneCommandeCreateRequest> ligneCommandes;
}