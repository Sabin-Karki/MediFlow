package com.project.MediFlow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
 private String externalId;
 private String patientExternalId;
 private String doctorExternalId;
 private String medicationName;
 private String notes;
 private LocalDate date;

}
