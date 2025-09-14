package com.project.MediFlow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PatientDTO {
      private String patient_id;
      private String firstName;
      private String lastName;
      private LocalDate DOB;

}
