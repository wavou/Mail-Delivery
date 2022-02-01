package com.picus.maildelivery.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Data
@Entity
@Table
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String email;

    @Column
    private String name;

    @Column
    private boolean isEmailSent;

    @Column
    private boolean isClickedLink;

    @Column
    private long sentDateOfMail;

    @Column
    private long clickDateOfLink;

    @Column
    private String verificationCode;


    public void createAndSetVerificationCode() {
        setVerificationCode(UUID.randomUUID().toString());
    }

    public void  handleClickVerification(){
        setClickedLink(true);
        setClickDateOfLink(Instant.now().getEpochSecond());
    }

    public void handleMailSent() {
        setEmailSent(true);
        setSentDateOfMail(Instant.now().getEpochSecond());
    }

    public long calculateDuration() {
        return getClickDateOfLink() - getSentDateOfMail();
    }
}
