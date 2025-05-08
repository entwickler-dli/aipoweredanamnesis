package com.anamnesis.demo.dao;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllergyRecord {
    private String allergen;
    private String reaction;
    private String severity;
}

