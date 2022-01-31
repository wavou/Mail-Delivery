package com.picus.maildelivery.mapper;

import com.picus.maildelivery.dto.ContactDTO;
import com.picus.maildelivery.entity.Contact;
import org.mapstruct.Mapping;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    @Mapping(target = "verificationCode", ignore = true)
    Contact toEntity(ContactDTO contactDTO);

    ContactDTO toDTO(Contact contact);

    List<ContactDTO> toDTOList(List<Contact> contactList);
}
