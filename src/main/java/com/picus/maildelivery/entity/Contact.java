package com.picus.maildelivery.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
public class Contact {

    @Id
    private String email;

    @Column
    private String name;

    //    private Campaign campaign;

    @Column(name = "is_email_sent")
    private Boolean isEmailSent;

    @Column(name = "is_clicked_link")
    private Boolean isClickedLink;

    @Column(name = "sent_date_of_mail")
    private LocalDateTime sentDateOfMail;

    @Column(name = "click_date_of_link")
    private LocalDateTime clickDateOfLink;

    @Column(name = "time_until_click")
    private Long timeUntilClick;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

}
