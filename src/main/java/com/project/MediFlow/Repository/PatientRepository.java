package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PatientRepository extends JpaRepository<Patient,Long> {

    Boolean existsByFirstAndLastName(String firstName, String lastName, LocalDate dob);
}
