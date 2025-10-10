package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.BillingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<BillingRecord,Long> {
    Boolean existsByExternalId(String externalId);
}
