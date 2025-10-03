package com.project.MediFlow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingRecordDTO {
private String externalId;
private String amount;
private String patientExternalId;
private LocalDate billingDate;
}
