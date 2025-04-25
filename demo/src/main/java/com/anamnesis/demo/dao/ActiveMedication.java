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
public class ActiveMedication {
    private String medicationName;
    private String dosage;
    private String prescribingDoctor;

    @Temporal(TemporalType.DATE)
    private Date startDate;
}

