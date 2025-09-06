package com.project.MediFlow.Config;

import com.project.MediFlow.Model.FieldAliasMapping;
import com.project.MediFlow.Repository.FieldAliasMappingRepository;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


// this class is to act as a dictionary for headers to be compared to so they can be transformed to fixed headers of correct respective entity

@Component
public class DataSeeder implements CommandLineRunner {

    private final  FieldAliasMappingRepository fieldAliasMappingRepository;
    public DataSeeder(FieldAliasMappingRepository fieldAliasMappingRepository){
        this.fieldAliasMappingRepository=fieldAliasMappingRepository;

    }
    @Override
    public void run(String... args) throws Exception {
                // this class always runs on startup so we have to make sure it does not  replicate data
        if(fieldAliasMappingRepository.count()==0){
            System.out.println(" Seeding the data in dictionary : ");
            //Patient Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient","firstname","firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient","fname","firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient","lastname","lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient","lname","lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient","dateofbirth","DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Patient", "dob","DOB"));
            //Doctor Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor","firstname","firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor","fname","firstName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor","lastname","lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor","lname","lastName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor","dateofbirth","DOB"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Doctor", "dob","DOB"));
            //Appointment Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment","date","date"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "appointmentid", "appointmentExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "apptid", "appointmentExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "patientid", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "pat_id", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "doctorid", "doctorExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Appointment", "doc_npi", "doctorExternalId"));

            //BillingRecord Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "amount", "amount"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "charge", "amount"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("BillingRecord", "patientid", "patientExternalId"));

            // LabResult Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "testname", "testName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "test", "testName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "result", "resultValue"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "value", "resultValue"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "unit", "unit"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "units", "unit"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("LabResult", "patientid", "patientExternalId"));

            //Prescription Alias
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "medicationname", "medicationName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "medication", "medicationName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "drug", "medicationName"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "notes", "notes"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "instructions", "notes"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "patientid", "patientExternalId"));
            fieldAliasMappingRepository.save(new FieldAliasMapping("Prescription", "doctorid", "doctorExternalId"));






        }
        }

}
