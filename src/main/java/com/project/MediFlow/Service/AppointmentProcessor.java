package com.project.MediFlow.Service;


import com.project.MediFlow.DTO.AppointmentDTO;
import com.project.MediFlow.Model.*;
import com.project.MediFlow.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentProcessor implements  FileProcessor{
//need to track all relationships here too
    // here CE has manytoone relation with doctor and patient while it has onetomany relation with appointment
    private final FieldAliasMappingRepository fieldAliasMappingRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ClinicalEncounterRepository clinicalEncounterRepository;

    public AppointmentProcessor(FieldAliasMappingRepository fieldAliasMappingRepository,AppointmentRepository appointmentRepository,DoctorRepository doctorRepository,PatientRepository patientRepository , ClinicalEncounterRepository clinicalEncounterRepository){
        this.fieldAliasMappingRepository=fieldAliasMappingRepository;
        this.appointmentRepository=appointmentRepository;
        this.doctorRepository=doctorRepository;
        this.patientRepository=patientRepository;
        this.clinicalEncounterRepository=clinicalEncounterRepository;
    }
    @Override
    @Transactional
    public void  process(RawDataEvent rawDataEvent){
      String path = rawDataEvent.getStoragePath();
        Path filePath = Paths.get(path);
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))){
            String headerLine = reader.readLine();
            String[]  header  = headerLine.split(",");

            //"preparing dictionary " <alias,fieldname>
            Map<String,String> aliasMapping = getAliasAndMappedField(getFileType()); //for accurate entitytype ofcourse,here for appointment file

            Map<Integer,String> columnIndexToField = new HashMap<>();
            for(int i = 0;i< header.length;i++){
                String rawHeader = header[i];
                String normalizedHeader = rawHeader.toLowerCase().trim();
                String field = aliasMapping.get(normalizedHeader);
                if(field!=null){
                    columnIndexToField.put(i,field);
                }
            }

            String dataLine;
            int rowNumber=1;
            while((dataLine= reader.readLine())!=null){
                rowNumber++; //1st row 1,dataline right after header becomes 2
                String[] data = dataLine.split(",");
                try{
                    AppointmentDTO dto = mapDataToDTO(data,columnIndexToField);
                    validateDTO(dto);

                    if(appointmentRepository.existsByExternalId(dto.getExternalId())){
                        continue; // skip the row if appointment already exists
                    }

//our appointment table has fk of ce,id,eid,date so we need to fetch the ce table too forits id to be stored in appointment table,,
                    //and the clinical encounter table has fk of doctor and patient ,so we need to fetch the patient and doctor object,and then set the ce table with those fk obj and because of the manytoone relation to doctor and patient the jpa recognizes and fills the id only

                    Doctor doctor = doctorRepository.findByExternalId(dto.getDoctorExternalId());
                    Patient patient = patientRepository.findByExternalId(dto.getPatientExternalId());
                    LocalDate date = dto.getAppointmentDate();
                    if(doctor==null || patient==null){
                        throw new IllegalArgumentException("Doctor or Patient not found for the given external id");

                    }
                    Optional<ClinicalEncounter> existingEncounter = clinicalEncounterRepository.findByDoctorAndPatientAndDate(doctor,patient,date);

                    ClinicalEncounter encounterToUse;
                    if(existingEncounter.isPresent()){
                        encounterToUse = existingEncounter.get();
                        System.out.println("Found existing encounter with  id " + encounterToUse.getId() );
                    }else {

                        ClinicalEncounter encounter = new ClinicalEncounter();
                        encounter.setDoctor(doctor);
                        encounter.setPatient(patient);
                        encounter.setDate(date);
                        encounterToUse = clinicalEncounterRepository.save(encounter);

                    }
                        Appointment newAppointment = new Appointment();
                        newAppointment.setExternalId(dto.getExternalId());
                        newAppointment.setDate(dto.getAppointmentDate());
                        newAppointment.setEncounter(encounterToUse);
                        appointmentRepository.save(newAppointment);


                }catch(Exception e){
                    System.err.println("Failed to process row number " + rowNumber + ": " + e.getMessage()) ;
                }
            }

        }catch(Exception e){
            throw new RuntimeException("Failed to process file: " + filePath, e);
        }

    }

@Override
    public String getFileType(){
    return "Appointment";
}

    private Map<String,String> getAliasAndMappedField(String entityType){
        List<FieldAliasMapping> mapping = fieldAliasMappingRepository.findByEntityType(entityType);
        return mapping.stream()
                .collect(Collectors.toMap(FieldAliasMapping::getAliasField,FieldAliasMapping::getMappedField));
    }

    private AppointmentDTO mapDataToDTO(String[] data , Map<Integer,String> plan){
        AppointmentDTO dto = new AppointmentDTO();
        for(Map.Entry<Integer,String> entry :plan.entrySet()){
            Integer index = entry.getKey();
            String field = entry.getValue();
            if(index<data.length){
                String value =data[index].trim();
                switch(field){
                    case"externalId":
                        dto.setExternalId(value);
                        break;
                    case "doctorExternalId":
                        dto.setDoctorExternalId(value);
                        break;
                    case "patientExternalId":
                        dto.setPatientExternalId(value);
                        break;
                    case "date":
                        dto.setAppointmentDate(LocalDate.parse(value));
                       break;
                    default:
                        System.out.println("unknown field : " + field + " will be ignored " );
                        break;
                }
            }
        }
        return dto;
    }
    private void validateDTO(AppointmentDTO dto) {
        if(dto.getExternalId()==null || dto.getExternalId().isEmpty()){
            throw new IllegalArgumentException("External id is missing or empty");
        }
        if(dto.getDoctorExternalId()==null || dto.getDoctorExternalId().isEmpty()){
            throw new IllegalArgumentException("Doctor external id is missing or empty");
        }
        if(dto.getPatientExternalId()==null|| dto.getPatientExternalId().isEmpty()){
            throw new IllegalArgumentException("Patient external id is missing or empty");
        }
        if(dto.getAppointmentDate()==null){
            throw new IllegalArgumentException("Date is missing");
        }
    }
}