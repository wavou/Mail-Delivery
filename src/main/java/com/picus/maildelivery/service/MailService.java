package com.picus.maildelivery.service;

import com.picus.maildelivery.dto.MailDTO;
import com.picus.maildelivery.entity.Contact;
import com.picus.maildelivery.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    @Value("${siteUrl}")
    private String siteURL;

    private final JavaMailSender mailSender;
    private final ContactRepository contactRepository;

    public boolean verify(String verificationCode) {
        Contact contact = contactRepository.findContactByVerificationCode(verificationCode);
        if(contact != null && Boolean.TRUE.equals(contact.getIsClickedLink())){
            return true;
        } else if(contact != null && Boolean.FALSE.equals(contact.getIsClickedLink())){
            contact.setIsClickedLink(true);
            contact.setClickDateOfLink(LocalDateTime.now());
            contact.setTimeUntilClick(Duration.between(contact.getClickDateOfLink(), contact.getSentDateOfMail()).toSeconds());
            contactRepository.save(contact);
            return true;
        }
        return false;
    }

    public void sendEmails(MailDTO mailDTO) {
        mailDTO.getContactEmails().forEach(email -> {
            //FIXME: is tis right implementation?
            Contact contact = contactRepository.findById(email).orElseThrow(EntityNotFoundException::new);
            //FIXME: try-catch is right here?
            try {
                sendEmail(mailDTO.getMailBody(), contact);

            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendEmail(String mailBody, Contact contact)
            throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "kyavuz@gmail.com";
        String senderName = "CompanyName";
        String subject = "Campaign";
        //FIXME : + string
        String content = mailBody + "\n" + "<a href=\"[[URL]]\" target=\"_self\">VERIFY</a>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(contact.getEmail());
        helper.setSubject(subject);

        //FIXME:
        String verifyURL = siteURL + "/api/verify?code=" + contact.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
        contact.setIsEmailSent(true);
        contact.setSentDateOfMail(LocalDateTime.now());
        contactRepository.save(contact);

        //TODO: Name could be added to logs.
        log.info("E mail sent to User: {}", contact);

    }


}
