package com.picus.maildelivery.service;

import com.picus.maildelivery.dto.ContactDTO;
import com.picus.maildelivery.entity.Contact;
import com.picus.maildelivery.mapper.Mapper;
import com.picus.maildelivery.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final Mapper mapper;
    private final ContactRepository repository;

    public ContactDTO createContact(ContactDTO contactDTO) {
        String verificationCode = UUID.randomUUID().toString();
        Contact contact = mapper.toEntity(contactDTO);
        contact.setVerificationCode(verificationCode);
        return mapper.toDTO(repository.save(contact));
    }

    public List<ContactDTO> getAllContacts() {

        return mapper.toDTOList(repository.findAll());
    }
}
