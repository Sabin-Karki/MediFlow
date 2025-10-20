package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {
  Boolean existsByExternalId(String externalId);
}
