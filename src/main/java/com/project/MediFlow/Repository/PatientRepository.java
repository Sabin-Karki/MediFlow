package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {

    Boolean existsByFirstNameAndLastName(String firstName, String lastName);

    Patient findByExternalId(String externalId);
}
