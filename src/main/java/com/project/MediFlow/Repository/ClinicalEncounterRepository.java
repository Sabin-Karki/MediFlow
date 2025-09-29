package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.ClinicalEncounter;
import com.project.MediFlow.Model.Doctor;
import com.project.MediFlow.Model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ClinicialEncounterRepository extends JpaRepository<ClinicalEncounter,Long> {
    Optional<ClinicalEncounter> findByDoctorAndPatientAndDate(Doctor doctor, Patient patient , LocalDateTime localDateTime);
}
