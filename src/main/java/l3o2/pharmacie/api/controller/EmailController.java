package l3o2.pharmacie.api.controller;

import jakarta.validation.Valid;
import l3o2.pharmacie.api.model.dto.request.EmailCommandeRequest;
import l3o2.pharmacie.api.model.dto.request.EmailSendingRequest;
import l3o2.pharmacie.api.model.dto.request.EmailWithAttachmentFileSendingRequest;
import l3o2.pharmacie.api.model.dto.request.EmailWithAttachmentPathSendingRequest;
import l3o2.pharmacie.api.service.CommandeService;
import l3o2.pharmacie.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour l'envoi d'emails.
 * @author raphaelcharoze
 * @version 1.0
 * @since 2025-03-30
 */

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final CommandeService commandeService;

    /**
     * Envoie un email simple.
     * @param request Les détails de l'email à envoyer.
     * @return Une ResponseEntity indiquant le résultat de l'opération.
     * @author raphaelcharoze
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody EmailSendingRequest request) {
        return emailService.sendSimpleMessage(request.getTo(), request.getSubject(), request.getText());
    }

    /**
     * Envoie un email au format HTML.
     * @param request Les détails de l'email HTML à envoyer.
     * @return Une ResponseEntity indiquant le résultat de l'opération.
     * @author raphaelcharoze
     */
    @PostMapping("/sendHtml")
    public ResponseEntity<?> sendEmailHtml(@Valid @RequestBody EmailSendingRequest request) {
        return emailService.sendHTMLEmail(request.getTo(), request.getSubject(), request.getText());
    }

    /**
     * Envoie un email avec une pièce jointe en utilisant un chemin de fichier.
     * @param request Les détails de l'email et le chemin de la pièce jointe.
     * @return Une ResponseEntity indiquant le résultat de l'opération.
     * @author raphaelcharoze
     */
    @PostMapping("/sendWithAttachmentPath")
    public ResponseEntity<?> sendEmailWithAttachmentPath(@Valid @RequestBody EmailWithAttachmentPathSendingRequest request) {
        return emailService.sendEmailWithAttachmentPath(request.getTo(), request.getSubject(), request.getText(), request.getFilePath());
    }

    /**
     * Envoie un email avec une pièce jointe en utilisant un fichier.
     * @param request Les détails de l'email et le fichier en pièce jointe.
     * @return Une ResponseEntity indiquant le résultat de l'opération.
     * @author raphaelcharoze
     */
    @PostMapping("/sendWithAttachmentFile")
    public ResponseEntity<?> sendEmailWithAttachmentFile(@Valid @RequestBody EmailWithAttachmentFileSendingRequest request) {
        return emailService.sendEmailWithAttachmentFile(request.getTo(), request.getSubject(), request.getText(), request.getFile());
    }

    /**
     * Envoie un email HTML avec une pièce jointe en utilisant un fichier.
     * @param request Les détails de l'email HTML et le fichier en pièce jointe.
     * @return Une ResponseEntity indiquant le résultat de l'opération.
     * @author raphaelcharoze
     */
    @PostMapping("/sendHtmlWithAttachmentFile")
    public ResponseEntity<?> sendHtmlEmailWithAttachmentFile(@Valid @RequestBody EmailWithAttachmentFileSendingRequest request) {
        return emailService.sendHTMLEmailWithAttachmentFile(request.getTo(), request.getSubject(), request.getText(), request.getFile());
    }

    /**
     * Envoie un email HTML avec une pièce jointe en utilisant un chemin de fichier.
     * @param request Les détails de l'email HTML et le chemin de la pièce jointe.
     * @return Une ResponseEntity indiquant le résultat de l'opération.
     * @author raphaelcharoze
     */
    @PostMapping("/sendHtmlWithAttachmentPath")
    public ResponseEntity<?> sendHtmlEmailWithAttachmentPath(@Valid @RequestBody EmailWithAttachmentPathSendingRequest request) {
        return emailService.sendHTMLEmailWithAttachmentPath(request.getTo(), request.getSubject(), request.getText(), request.getFilePath());
    }

    /**
     * Envoie un email de commande au fournisseur.
     * @param request La requête contenant la référence de la commande.
     * @return Une ResponseEntity indiquant le résultat de l'opération.
     */
    @PostMapping("/sendCommandeAuFournisseur")
    public ResponseEntity<?> sendCommandeAuFournisseur(@Valid @RequestBody EmailCommandeRequest request) {
        return emailService.sendCommandeEmailToFournisseur(request.getCommandeReference());
    }
}
