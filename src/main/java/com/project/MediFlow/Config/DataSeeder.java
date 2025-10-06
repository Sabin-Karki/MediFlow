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
            System.out.println("Seeding the data dictionary with a comprehensive set of aliases:");

         // Patient
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "firstname", "firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "first_name", "firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "fname", "firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "given_name", "firstName"));


            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "lastname", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "last_name", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "lname", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "surname", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "family_name", "lastName"));

            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "dob", "DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "date_of_birth", "DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "birthdate", "DOB"));


            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "id", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "externalid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "patient_id", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "patientid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "pid", "externalId"));


            // =================================================================
            // Doctor Aliases
            // =================================================================
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "firstname", "firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "first_name", "firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "lastname", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "last_name", "lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "id", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor","dob","DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "date_of_birth", "DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "externalid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "doctor_id", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "doctorid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "docid", "externalId"));


            // =================================================================
            // Appointment Aliases
            // =================================================================
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "date", "date"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "appointment_date", "date"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "appt_date", "date"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "appointment_id", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "apptid", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "patient_id", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "patid", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "doctor_id", "doctorExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "docid", "doctorExternalId"));


            // =================================================================
            // BillingRecord Aliases
            // =================================================================
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "amount", "amount"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "charge", "amount"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "cost", "amount"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "price", "amount"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "billing_date", "date"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "date", "date"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "billing_id", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "patient_id", "patientExternalId"));


            // =================================================================
            // LabResult Aliases
            // =================================================================
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "test_name", "testName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "test", "testName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "result", "resultValue"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "value", "resultValue"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "units", "unit"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "unit", "unit"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "test_date", "testDate"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "date", "testDate"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "lab_id", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "patient_id", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "doctor_id", "doctorExternalId"));


            // =================================================================
            // Prescription Aliases
            // =================================================================
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "medication", "medicationName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "drug", "medicationName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "med_name", "medicationName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "notes", "notes"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "instructions", "notes"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "date", "date"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "prescription_date", "date"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "prescription_id", "externalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "patient_id", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "doctor_id", "doctorExternalId"));

            System.out.println("Dictionary seeding complete.");

        } else {
            System.out.println("Data dictionary already exists. Skipping seed.");
        }
    }
}
