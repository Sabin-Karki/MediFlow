package com.project.MediFlow.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabResultDTO {

    private String externalId;
    private String patientExternalId;
    private String doctorExternalId;
    private String testName;
    private String resultValue;
    private String unit;
    private LocalDate testDate;
}
