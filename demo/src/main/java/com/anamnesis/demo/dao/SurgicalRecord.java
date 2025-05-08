package com.anamnesis.demo.dao;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgicalRecord {
    private String procedure;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String notes;
}

