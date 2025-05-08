package com.anamnesis.demo.repository;

import com.anamnesis.demo.dao.Patient;
import com.anamnesis.demo.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findPatientByUser(User user);
}
