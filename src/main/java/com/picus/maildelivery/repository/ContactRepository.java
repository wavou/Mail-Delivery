package com.picus.maildelivery.repository;

import com.picus.maildelivery.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

     Optional<Contact> findContactByVerificationCode(String verificationCode);

     Optional<Contact> findContactByEmail(String email);
}
