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
 * Controller for sending emails
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
     * Send a simple email
     * @author raphaelcharoze
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody EmailSendingRequest request) {
        return emailService.sendSimpleMessage(request.getTo(), request.getSubject(), request.getText());
    }

    /**
     * Send an HTML email
     * @author raphaelcharoze
     */
    @PostMapping("/sendHtml")
    public ResponseEntity<?> sendEmailHtml(@Valid @RequestBody EmailSendingRequest request) {
        return emailService.sendHTMLEmail(request.getTo(), request.getSubject(), request.getText());
    }

    /**
     * Send an email with an attachment using a file path
     * @author raphaelcharoze
     */
    @PostMapping("/sendWithAttachmentPath")
    public ResponseEntity<?> sendEmailWithAttachmentPath(@Valid @RequestBody EmailWithAttachmentPathSendingRequest request) {
        return emailService.sendEmailWithAttachmentPath(request.getTo(), request.getSubject(), request.getText(), request.getFilePath());
    }

    /**
     * Send an email with an attachment using a file
     * @author raphaelcharoze
     */
    @PostMapping("/sendWithAttachmentFile")
    public ResponseEntity<?> sendEmailWithAttachmentFile(@Valid @RequestBody EmailWithAttachmentFileSendingRequest request) {
        return emailService.sendEmailWithAttachmentFile(request.getTo(), request.getSubject(), request.getText(), request.getFile());
    }

    /**
     * Send an HTML email with an attachment using a file
     * @author raphaelcharoze
     */
    @PostMapping("/sendHtmlWithAttachmentFile")
    public ResponseEntity<?> sendHtmlEmailWithAttachmentFile(@Valid @RequestBody EmailWithAttachmentFileSendingRequest request) {
        return emailService.sendHTMLEmailWithAttachmentFile(request.getTo(), request.getSubject(), request.getText(), request.getFile());
    }

    /**
     * Send an HTML email with an attachment using a file path
     * @author raphaelcharoze
     */
    @PostMapping("/sendHtmlWithAttachmentPath")
    public ResponseEntity<?> sendHtmlEmailWithAttachmentPath(@Valid @RequestBody EmailWithAttachmentPathSendingRequest request) {
        return emailService.sendHTMLEmailWithAttachmentPath(request.getTo(), request.getSubject(), request.getText(), request.getFilePath());
    }

    @PostMapping("/sendCommandeAuFournisseur")
    public ResponseEntity<?> sendCommandeAuFournisseur(@Valid @RequestBody EmailCommandeRequest request) {
        return emailService.sendCommandeEmailToFournisseur(request.getCommandeReference());
    }
}
