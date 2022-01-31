package com.picus.maildelivery.dto;

import lombok.Data;

@Data
public class ContactDTO {
    private String email;
    private String name;
//    private Campaign campaign;

    private Boolean isEmailSent;
    private Boolean isClickedLink;
    private Long durationOfClick;
}
