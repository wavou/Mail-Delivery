package com.picus.maildelivery.service;

import com.picus.maildelivery.constants.LinkStatus;
import com.picus.maildelivery.entity.Contact;
import com.picus.maildelivery.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import static com.picus.maildelivery.util.ContactAndMailEntityFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class MailServiceTest {


    private MailService mailService;
    @Mock private ContactRepository repository;
    @Mock private JavaMailSender mailSender;


    @BeforeEach
    void init() {
        mailService = new MailService(mailSender, repository);
    }

    @Test
    void verify_itShouldVerifyLinkSuccessfully() {

        Contact contact = createContact();

        when(repository.findContactByVerificationCode(Mockito.any())).thenReturn(Optional.of(contact));
        String linkStatus = mailService.verify(contact.getVerificationCode());
        assertEquals(LinkStatus.SUCCESS.getKey(), linkStatus);
    }

    @Test
    void verify_itShouldReturnAlreadyClickedLinkStatus() {

        Contact contact = createContact();
        contact.setClickedLink(true);
        when(repository.findContactByVerificationCode(Mockito.any())).thenReturn(Optional.of(contact));
        String linkStatus = mailService.verify(contact.getVerificationCode());
        assertEquals(LinkStatus.ALREADY_CLICKED.getKey(), linkStatus);
    }

    @Test
    void verify_itShouldThrowEntityNotFoundExceptionWhenVerificationCodeIsNull() {

        Contact contact = createContact();
        contact.setVerificationCode(null);
        assertThrows(EntityNotFoundException.class, ()-> mailService.verify(contact.getVerificationCode()));
    }


    @Test
    void sendEmails_itShouldThrowNullPointerExceptionWhenMailInfoNotProvided() {
        List<Contact> contactList = createContactList();
        when(repository.findContactByEmail(Mockito.any())).thenReturn(Optional.of(contactList));
        assertThrows(NullPointerException.class, ()-> mailService.sendEmails(createMailDTO()));
    }

    @Test
    void sendEmail_itShouldThrowNullPointerExceptionWhenMailInfoNotProvided() {
        Contact contact = createContact();
        contact.setEmailSent(false);
        String mailBody = "mail body";

        when(repository.save(Mockito.any())).thenReturn(Optional.of(contact));
        assertThrows(NullPointerException.class, ()-> mailService.sendEmail(mailBody,contact));

    }

    @Test
    void sendEmailToAddress_itShouldThrowNullPointerExceptionWhenSenderNameIsNull(){
        Contact contact = createContact();
        contact.setEmailSent(false);
        String mailBody = "mail body";
        String subject =" subject";

        when(repository.save(Mockito.any())).thenReturn(Optional.of(contact));
        assertThrows(NullPointerException.class, ()-> mailService.sendEmailToAdress(mailBody,
                contact.getEmail(), contact.getVerificationCode(), subject));

    }



}