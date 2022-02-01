package com.picus.maildelivery.service;

import com.picus.maildelivery.constants.LinkStatus;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private static final  String VERIFICATION_LINK = "\n<a href=\"[[URL]]\" target=\"_self\">VERIFY</a>";
    private static final  String VERIFY_ENDPOINT = "/api/verify?code=";

    @Value("${siteUrl}")
    private String siteURL;

    @Value("${mailFromAddress}")
    private String fromAddress;

    @Value("${mailSenderName}")
    private String senderName;

    private final JavaMailSender mailSender;
    private final ContactRepository contactRepository;


    public String verify(String verificationCode) {
        Contact contact = contactRepository.findContactByVerificationCode(verificationCode).
                orElseThrow(EntityNotFoundException::new);
        if(contact.isClickedLink()){
            return LinkStatus.ALREADY_CLICKED.getKey();
        } else {
            contact.handleClickVerification();
            contactRepository.save(contact);
            return LinkStatus.SUCCESS.getKey();
        }
    }

    public void sendEmails(MailDTO mailDTO) throws MessagingException, UnsupportedEncodingException {
        for(String email: mailDTO.getContactEmails()){
            Contact contact = contactRepository.findContactByEmail(email).orElseThrow(EntityNotFoundException::new);
            sendEmail(mailDTO.getMailBody(), contact);
        }
    }

    public void sendEmail(String mailBody, Contact contact)
            throws MessagingException, UnsupportedEncodingException {
        String subject = "Campaign";

        if(!contact.isEmailSent()){
            sendEmailToAdress(mailBody, contact.getEmail(),contact.getVerificationCode(), subject);
            contact.handleMailSent();
            contactRepository.save(contact);
            log.info("E mail sent to User: {}", contact);
        }

    }

    public void sendEmailToAdress(String mailBody, String email, String verificationCode, String subject)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("mailAddr", senderName);
        helper.setTo(email);
        helper.setSubject(subject);

        helper.setText(createMailContent(mailBody, verificationCode), true);

        mailSender.send(message);

    }

    private String createMailContent(String mailBody, String verificationCode) {

        String content = mailBody + VERIFICATION_LINK;
        String verifyURL = siteURL + VERIFY_ENDPOINT + verificationCode;

        return content.replace("[[URL]]", verifyURL);

    }

}
