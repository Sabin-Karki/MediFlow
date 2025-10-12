package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    Boolean existsByFirstNameAndLastName(String firstName, String lastName);

    Doctor findByExternalId(String externalId);
}

