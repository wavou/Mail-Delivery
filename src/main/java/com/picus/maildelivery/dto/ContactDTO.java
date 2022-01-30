package com.picus.maildelivery.dto;

import lombok.Data;

@Data
public class ContactDTO {
    private String email;
    private String name;
//    private Campaign campaign;
    private boolean isClickedLink;
    private long durationOfClick;
}
