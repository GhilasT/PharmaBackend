package l3o2.pharmacie.util;

import l3o2.pharmacie.api.model.dto.response.CommandeResponse;
import l3o2.pharmacie.api.model.dto.response.LigneCommandeResponse;
import l3o2.pharmacie.api.repository.PharmacienAdjointRepository;
import l3o2.pharmacie.api.service.PharmacienAdjointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final PharmacienAdjointService pharmacienAdjointService;

    public String commandeToHtmlEmail(CommandeResponse commande) {
        StringBuilder sb = new StringBuilder();

        sb
                .append("<h1>Commande - ref. ").append(commande.getReference()).append("</h1>")
                .append("<p> Date de commande : ").append(commande.getDateCommande()).append(".</p>")
                .append("<p> Commande effectué par ").append(pharmacienAdjointService.findById(commande.getPharmacienAdjointId()).getPrenom()).append(" ").append(pharmacienAdjointService.findById(commande.getPharmacienAdjointId()).getNom()).append(".<p>")
                .append("<p> Montant total: ").append(commande.getMontantTotal()).append("- EUR.</p>");


        sb.append("<h3>Détails : </h3>");
        sb.append("<table border=\"1\"><center>");
        sb.append("<tr>")
                .append("<td><b>Médicament</b></td><td><b>Quantité</b></td><td><b>Prix Unitaire</b></td><td><b>Total HT</b></td>")//<td><b>Taux TVA</b></td><td><b>Total TTC</b></td>")
                .append("</tr>");

        for (LigneCommandeResponse ligne : commande.getLigneCommandes())  sb.append(ligneCommandeToPartHtmlEmail(ligne));

        sb.append("</center></table>");

        return sb.toString();
    }

    public String ligneCommandeToPartHtmlEmail(LigneCommandeResponse ligne) {

        return "<tr>" +
                "<td>" + ligne.getStockMedicamentDTO().getLibelle() + "</td>" +
                "<td>" + ligne.getQuantite() + "</td>" +
                "<td>" + ligne.getPrixUnitaire() + "</td>" +
                "<td>" + (ligne.getPrixUnitaire()).doubleValue() * ligne.getQuantite() + "</td>" +
                //"<td>" + ligne.getMedicament().getPresentation().getTaxe() + "</td>" +
                //"<td>" + ligne.getPrixUnitaire() * ligne.getQuantite() * 1.1 + "</td>" + //ligne.getMedicament().getPresentation().getTaxe().doubleValue() + "</td>" +
                "</tr>";
    }

}
