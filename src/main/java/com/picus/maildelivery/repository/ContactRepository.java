package com.picus.maildelivery.repository;

import com.picus.maildelivery.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

}
