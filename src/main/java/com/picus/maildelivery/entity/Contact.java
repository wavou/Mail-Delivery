package com.picus.maildelivery.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class Contact {

    @Id
    private String email;
    @Column
    private String name;
    //    private Campaign campaign;
    @Column
    private boolean isClickedLink;
    @Column
    private long durationOfClick;
}
