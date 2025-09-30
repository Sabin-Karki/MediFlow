package com.project.MediFlow.Config;

import com.project.MediFlow.Model.FieldAliasMapping;
import com.project.MediFlow.Repository.FieldAliasMappingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final FieldAliasMappingRepository fieldAliasMappingRepository;

    public DataSeeder(FieldAliasMappingRepository fieldAliasMappingRepository) {
        this.fieldAliasMappingRepository = fieldAliasMappingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (fieldAliasMappingRepository.count() == 0) {
            System.out.println("Seeding the data in dictionary:");

            // Patient Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "firstname", "firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "fname", "firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "lastname", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "lname", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "dateofbirth", "DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "dob", "DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "patientexternalid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "externalid", "externalId"));

            // Doctor Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "firstname", "firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "fname", "firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "lastname", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "lname", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "dateofbirth", "DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "dob", "DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "doctorexternalid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "externalid", "externalId"));

            // Appointment Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "date", "date"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "appointmentid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "apptid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "externalid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "patientid", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "patid", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "doctorid", "doctorExternalId"));

            // BillingRecord Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "amount", "amount"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "charge", "amount"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "patientid", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "externalid", "externalId"));

            // LabResult Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "testname", "testName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "test", "testName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "result", "resultValue"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "value", "resultValue"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "unit", "unit"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "units", "unit"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "patientid", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "doctorid", "doctorExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "externalid", "externalId"));

            // Prescription Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "medicationname", "medicationName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "medication", "medicationName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "drug", "medicationName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "notes", "notes"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "instructions", "notes"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "patientid", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "doctorid", "doctorExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "externalid", "externalId"));

        } else {
            System.out.println("Data already seeded");
        }
    }
}
