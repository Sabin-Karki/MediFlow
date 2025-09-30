package com.project.MediFlow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
private String externalId;
private String patientExternalId;
private String doctorExternalId;
private LocalDate appointmentDate;
}
