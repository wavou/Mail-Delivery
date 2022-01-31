package com.picus.maildelivery.dto;

import lombok.Data;

import java.util.List;

@Data
public class MailDTO {
    private List<String> contactEmails;
    private String mailBody;
}
