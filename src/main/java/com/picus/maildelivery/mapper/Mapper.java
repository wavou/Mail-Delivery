package com.picus.maildelivery.mapper;

import com.picus.maildelivery.dto.ContactDTO;
import com.picus.maildelivery.entity.Contact;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    @Mapping(target = "verificationCode", ignore = true)
    @Mapping(target = "id", ignore = true)
    Contact toEntity(ContactDTO contactDTO);

    @Mapping(source = "contact", target = "durationUntilClick", qualifiedByName = "calculateDuration")
    ContactDTO toDTO(Contact contact);

    @Mapping(target = "id", ignore = true)
    void update( ContactDTO contactDTO, @MappingTarget Contact contactEntity);

    List<ContactDTO> toDTOList(List<Contact> contactList);

    @Named("calculateDuration")
    default long calculateDuration(Contact contact) {
        return contact.calculateDuration();
    }

}
