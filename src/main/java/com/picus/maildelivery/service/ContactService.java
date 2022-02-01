package com.picus.maildelivery.service;

import com.picus.maildelivery.dto.ContactDTO;
import com.picus.maildelivery.entity.Contact;
import com.picus.maildelivery.mapper.Mapper;
import com.picus.maildelivery.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final Mapper mapper;
    private final ContactRepository repository;

    public ContactDTO createContact(ContactDTO contactDTO) {
        Contact contact = mapper.toEntity(contactDTO);
        contact.createAndSetVerificationCode();
        return mapper.toDTO(repository.save(contact));
    }

    public List<ContactDTO> createContacts(List<ContactDTO> contacts) {

        contacts.forEach(contactDTO -> {

            Optional<Contact> contact = repository.findById(contactDTO.getId());
            if(contact.isPresent()){
               mapper.update(contactDTO, contact.get());
               repository.save(contact.get());
            } else {
                Contact contactToCreate = mapper.toEntity(contactDTO);
                contactToCreate.createAndSetVerificationCode();
                repository.save(contactToCreate);
            }

        });

        return mapper.toDTOList(repository.findAll());
    }

    public List<ContactDTO> getAllContacts() {
        return mapper.toDTOList(repository.findAll());
    }

    public ContactDTO updateContact(ContactDTO contactDTO) {
        Contact entity = repository.findById(contactDTO.getId()).orElseThrow(EntityNotFoundException::new);
        mapper.update(contactDTO, entity);
        return mapper.toDTO(repository.save(entity));
    }
}
