package com.picus.maildelivery.controller;

import com.picus.maildelivery.dto.ContactDTO;
import com.picus.maildelivery.dto.MailDTO;
import com.picus.maildelivery.service.ContactService;
import com.picus.maildelivery.service.MailService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;


@RestController
@RequestMapping(value = "api")
@Slf4j
@Data
public class ApiController {

    private final ContactService contactService;
    private final MailService mailService;

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactDTO>> getAllContacts() {
        log.info("Get all contacts.");
        List<ContactDTO> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @PostMapping("/create-contacts")
    public ResponseEntity<List<ContactDTO>> createContacts(@RequestBody List<ContactDTO> contacts) {
        log.info("create-contacts : {}", contacts);
        return ResponseEntity.ok(contactService.createContacts(contacts));
    }

    @PostMapping("/create-contact")
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contact) {
        log.info("create-contact : {}", contact);
        return ResponseEntity.ok(contactService.createContact(contact));
    }

    @PutMapping("/update-contact")
    public ResponseEntity<ContactDTO> updateContact(@RequestBody ContactDTO contactDTO) {
        log.info("update-contact : {}", contactDTO);
        return ResponseEntity.ok(contactService.updateContact(contactDTO));
    }


    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody MailDTO emailsToSend) {
        log.info("send-email : {}", emailsToSend);
        try {
            mailService.sendEmails(emailsToSend);
        } catch (MessagingException |
                UnsupportedEncodingException |
                MailAuthenticationException |
                EntityNotFoundException e) {
           return ResponseEntity.badRequest().body("Mail could not send");
        }
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/verify")
    public String redirectLink(@Param("code") String code) {
        return mailService.verify(code);
    }


}
