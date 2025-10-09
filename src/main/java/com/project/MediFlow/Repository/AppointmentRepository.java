package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    Boolean existsByExternalId(String externalId);

}
