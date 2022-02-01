package com.picus.maildelivery.util;

import com.picus.maildelivery.dto.ContactDTO;
import com.picus.maildelivery.dto.MailDTO;
import com.picus.maildelivery.entity.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContactAndMailEntityFactory {

    private static final long ID = 1;
    private static final String NAME = "Someone's Name";
    private static final String E_MAIL = "someaddress@mail.com";
    private static final String VERIFICATION_CODE = String.valueOf(UUID.randomUUID());
    private static final long DURATION_UNTIL_CLICK = 1000;
    private static final boolean IS_LINK_CLICKED = false;
    private static final boolean IS_MAIL_SENT = false;
    private static final long CLICK_DATE_OF_LINK = 1500;
    private static final long SENT_DATE_OF_MAIL = 500;

    public static Contact createContact(){
        Contact contact = new Contact();
        contact.setId(ID);
        contact.setName(NAME);
        contact.setEmail(E_MAIL);
        contact.setClickedLink(IS_LINK_CLICKED);
        contact.setEmailSent(IS_MAIL_SENT);
        contact.setVerificationCode(VERIFICATION_CODE);
        contact.setClickDateOfLink(CLICK_DATE_OF_LINK);
        contact.setSentDateOfMail(SENT_DATE_OF_MAIL);
        return contact;
    }

    public static ContactDTO createContactDTO(){
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(ID);
        contactDTO.setName(NAME);
        contactDTO.setEmail(E_MAIL);
        contactDTO.setClickedLink(IS_LINK_CLICKED);
        contactDTO.setEmailSent(IS_MAIL_SENT);
        contactDTO.setDurationUntilClick(DURATION_UNTIL_CLICK);
        return contactDTO;
    }

    public static List<ContactDTO> createContactDTOList() {
        List<ContactDTO> contactDTOList = new ArrayList<>();
        contactDTOList.add(createContactDTO());
        return contactDTOList;
    }

    public static List<Contact> createContactList() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(createContact());
        return contactList;
    }

    public static List<String> createMailAddresses(){
        List<String> emails = new ArrayList<>();
        emails.add("somemail@mail.com");
        emails.add("goodmail@hooray.com");
        return emails;
    }

    public static MailDTO createMailDTO() {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setMailBody("mail body");

        mailDTO.setContactEmails(createMailAddresses());
        return mailDTO;
    }
}
