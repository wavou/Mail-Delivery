package com.picus.maildelivery.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ContactDTO {

    @NotNull
    private long id;
    @NotNull
    private String email;
    @NotNull
    private String name;

    private boolean isEmailSent;

    private boolean isClickedLink;

    private long durationUntilClick;
}
