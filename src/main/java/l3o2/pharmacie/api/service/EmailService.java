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
 * Service for sending emails
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

    /**
     * Send a simple email
     * @param to destinataire
     * @param subject sujet
     * @param text corps du mail
     * @return ResponseEntity de l'email envoyé
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
     * Send an email with an attachment
     * @param to destinataire
     * @param subject sujet
     * @param text corps du mail
     * @param filePath chemin du fichier à envoyer
     * @return ResponseEntity de l'email envoyé
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
     * Send an email with an attachment
     * @param to destinataire
     * @param subject sujet
     * @param text corps du mail
     * @param file chemin du fichier à envoyer
     * @return ResponseEntity de l'email envoyé
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
     * Send an HTML email
     * @param to destinataire
     * @param subject sujet
     * @param htmlBody corps du mail
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
     * Send an HTML email with an attachment (given by a path)
     * @param to destinataire
     * @param subject sujet
     * @param htmlBody corps du mail
     * @param filePath chemin du fichier à envoyer
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
     * Send an HTML email with an attachment (given by a file)
     * @param to destinataire
     * @param subject sujet
     * @param htmlBody corps du mail
     * @param file fichier à envoyer
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
     *  Envoie un email au fournisseur à partir d'une commande
     * @author raphaelcharoze
     */
    public ResponseEntity<?> sendCommandeEmailToFournisseur(String commandeRef) {
        CommandeResponse commande = commandeService.getCommande(UUID.fromString(commandeRef));
        String fournisseurEmail = "raphaelcharoze@gmail.com"; //commande.getFournisseur().getEmail();
        String subject = "Commande - ref." + commandeRef;
        String body = emailUtil.commandeToHtmlEmail(commande);
        System.out.println(body);
        return sendHTMLEmail(fournisseurEmail,subject, body);
    }
}
