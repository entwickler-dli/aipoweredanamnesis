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
public class LabResult {
    private String testType;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String keyFindings; // or abnormal values

    private String images; // could be a URL, file reference, or path
}

