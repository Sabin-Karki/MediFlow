package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.ClincialEncounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicialEncounterRepository extends JpaRepository<ClincialEncounter,Long> {
}
