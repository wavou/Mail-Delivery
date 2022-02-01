package com.picus.maildelivery.service;

import com.picus.maildelivery.dto.ContactDTO;
import com.picus.maildelivery.entity.Contact;
import com.picus.maildelivery.mapper.MapperImpl;
import com.picus.maildelivery.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static com.picus.maildelivery.util.ContactAndMailEntityFactory.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContactServiceTest {

    private ContactService contactService;
    @Mock private ContactRepository repository;

    @BeforeEach
    void init() {
        contactService = new ContactService(new MapperImpl(), repository);
    }

    @Test
    void createContact_itShouldCreateContactSuccessfully() {
        ContactDTO contactDTO = createContactDTO();
        Contact contact = createContact();

        when(repository.save(Mockito.any())).thenReturn(contact);
        ContactDTO resultDTO = contactService.createContact(contactDTO);

        assertEquals(resultDTO, contactDTO);
    }

    @Test
    void createContacts_itShouldCreateContactsSuccessfully() {
        Contact contact = createContact();
        List<ContactDTO> contactDTOList = createContactDTOList();
        List<Contact> contactList = createContactList();
        when(repository.save(Mockito.any())).thenReturn(contact);
        when(repository.findAll()).thenReturn(contactList);
        List<ContactDTO> result = contactService.createContacts(contactDTOList);

        assertEquals(result, contactDTOList);
    }

    @Test
     void getAllContacts_shouldReturnContactListSuccessfully() {
        List<Contact> contactList = createContactList();
        when(repository.findAll()).thenReturn(contactList);
        List<ContactDTO> result = contactService.getAllContacts();
        assertEquals(contactList.get(0).getId(), result.get(0).getId());
        assertEquals(contactList.get(0).getEmail(), result.get(0).getEmail());
        assertEquals(contactList.get(0).getName(), result.get(0).getName());
        assertEquals(contactList.get(0).isClickedLink(), result.get(0).isClickedLink());
        assertEquals(contactList.get(0).isEmailSent(), result.get(0).isEmailSent());
        assertEquals(contactList.get(0).calculateDuration(), result.get(0).getDurationUntilClick());
    }

    @Test
    void updateCotact_shouldUpdateContactSuccessfully() {
        Contact contact = createContact();
        ContactDTO contactDTO = createContactDTO();
        contactDTO.setName("updated name");
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(contact));
        when(repository.save(Mockito.any())).thenReturn(contact);
        ContactDTO result = contactService.updateContact(contactDTO);
        assertEquals("updated name", result.getName());
        assertEquals(result, contactDTO);
    }

}