package com.picus.maildelivery.controller;

import com.picus.maildelivery.dto.ContactDTO;
import com.picus.maildelivery.dto.MailDTO;
import com.picus.maildelivery.service.ContactService;
import com.picus.maildelivery.service.MailService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/create-contact")
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        log.info("create-contact : {}", contactDTO);
        contactService.createContact(contactDTO);
        return ResponseEntity.ok(contactDTO);
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody MailDTO emailsToSend) {
        log.info("send-email : {}", emailsToSend);
        mailService.sendEmails(emailsToSend);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/verify")
    public String redirectLink(@Param("code") String code) {
        if (mailService.verify(code)) {
            return "redirect_success";
        } else {
            return "redirect_fail";
        }
    }


}
