package com.anamnesis.demo.repository;

import com.anamnesis.demo.dao.MedicalHistory;
import com.anamnesis.demo.dao.Patient;
import com.anamnesis.demo.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Integer> {

    Optional<MedicalHistory> findByPatient(Patient patient);
}
