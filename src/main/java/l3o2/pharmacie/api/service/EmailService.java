package l3o2.pharmacie.api.service;

import jakarta.mail.internet.MimeMessage;
import l3o2.pharmacie.api.model.dto.response.CommandeResponse;
import l3o2.pharmacie.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

/**
 * Service pour l'envoi d'e-mails.
 * Gère différents types d'e-mails : simples, avec pièces jointes, et au format HTML.
 * @author raphaelcharoze
 * @version 1.0
 * @since 2025-03-30
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final EmailUtil emailUtil;
    @Autowired
    private CommandeService commandeService;
    private final FournisseurService fournisseurService; // Added FournisseurService

    /**
     * Envoie un e-mail simple.
     * @param to Le destinataire de l'e-mail.
     * @param subject Le sujet de l'e-mail.
     * @param text Le corps du message de l'e-mail.
     * @return Une {@link ResponseEntity} indiquant le succès ou l'échec de l'envoi.
     * @author raphaelcharoze
     */
    public ResponseEntity<?> sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            return ResponseEntity.ok().body("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /**
     * Envoie un e-mail avec une pièce jointe spécifiée par son chemin d'accès.
     * @param to Le destinataire de l'e-mail.
     * @param subject Le sujet de l'e-mail.
     * @param text Le corps du message de l'e-mail.
     * @param filePath Le chemin d'accès au fichier à joindre.
     * @return Une {@link ResponseEntity} indiquant le succès ou l'échec de l'envoi.
     * @author raphaelcharoze
     */
    public ResponseEntity<?> sendEmailWithAttachmentPath(String to, String subject, String text, String filePath) {
        try{

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(file.getFilename(), file);

            emailSender.send(message);

            return ResponseEntity.ok().body("Email with attachment sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Envoie un e-mail avec une pièce jointe de type {@link File}.
     * @param to Le destinataire de l'e-mail.
     * @param subject Le sujet de l'e-mail.
     * @param text Le corps du message de l'e-mail.
     * @param file Le fichier à joindre.
     * @return Une {@link ResponseEntity} indiquant le succès ou l'échec de l'envoi.
     * @author raphaelcharoze
     */
    public ResponseEntity<?> sendEmailWithAttachmentFile(String to, String subject, String text, File file) {
        try{

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            helper.addAttachment(file.getName(), file);

            emailSender.send(message);

            return ResponseEntity.ok().body("Email with attachment sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Envoie un e-mail au format HTML.
     * @param to Le destinataire de l'e-mail.
     * @param subject Le sujet de l'e-mail.
     * @param htmlBody Le corps de l'e-mail au format HTML.
     * @return Une {@link ResponseEntity} indiquant le succès ou l'échec de l'envoi.
     */
    public ResponseEntity<?> sendHTMLEmail(String to, String subject, String htmlBody) {
        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            emailSender.send(message);

            return ResponseEntity.ok().body("Email with HTML body sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Envoie un e-mail au format HTML avec une pièce jointe spécifiée par son chemin d'accès.
     * @param to Le destinataire de l'e-mail.
     * @param subject Le sujet de l'e-mail.
     * @param htmlBody Le corps de l'e-mail au format HTML.
     * @param filePath Le chemin d'accès au fichier à joindre.
     * @return Une {@link ResponseEntity} indiquant le succès ou l'échec de l'envoi.
     */
    public ResponseEntity<?> sendHTMLEmailWithAttachmentPath(String to, String subject, String htmlBody, String filePath) {
        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(file.getFilename(), file);

            emailSender.send(message);

            return ResponseEntity.ok().body("Email with HTML body and attachment sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Envoie un e-mail au format HTML avec une pièce jointe de type {@link File}.
     * @param to Le destinataire de l'e-mail.
     * @param subject Le sujet de l'e-mail.
     * @param htmlBody Le corps de l'e-mail au format HTML.
     * @param file Le fichier à joindre.
     * @return Une {@link ResponseEntity} indiquant le succès ou l'échec de l'envoi.
     */
    public ResponseEntity<?> sendHTMLEmailWithAttachmentFile(String to, String subject, String htmlBody, File file) {
        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            helper.addAttachment(file.getName(), file);

            emailSender.send(message);

            return ResponseEntity.ok().body("Email with HTML body and attachment sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     *  Envoie un e-mail au fournisseur à partir des informations d'une commande.
     *  L'e-mail contient les détails de la commande au format HTML.
     * @param commandeRef La référence (UUID sous forme de String) de la commande.
     * @return Une {@link ResponseEntity} indiquant le succès ou l'échec de l'envoi.
     * @author raphaelcharoze
     */
    public ResponseEntity<?> sendCommandeEmailToFournisseur(String commandeRef) {
        CommandeResponse commande = commandeService.getCommande(UUID.fromString(commandeRef));
        String fournisseurEmail = fournisseurService.getFournisseurById(commande.getFournisseurId()).getEmail();
        String subject = "Commande - ref." + commandeRef;
        String body = emailUtil.commandeToHtmlEmail(commande);
        System.out.println(body);
        return sendHTMLEmail(fournisseurEmail, subject, body);
    }
}
