package com.project.MediFlow.Service;

import com.project.MediFlow.DTO.PrescriptionDTO;
import com.project.MediFlow.Model.*;
import com.project.MediFlow.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionProcessor implements FileProcessor{
private final ClinicalEncounterRepository clinicalEncounterRepository;
private final PrescriptionRepository prescriptionRepository;
private final FieldAliasMappingRepository fieldAliasMappingRepository;
private final PatientRepository patientRepository;
private final DoctorRepository doctorRepository;
public PrescriptionProcessor(ClinicalEncounterRepository clinicalEncounterRepository,PrescriptionRepository prescriptionRepository,FieldAliasMappingRepository fieldAliasMappingRepository,PatientRepository patientRepository,DoctorRepository doctorRepository){
    this.clinicalEncounterRepository=clinicalEncounterRepository;
    this.prescriptionRepository=prescriptionRepository;
    this.fieldAliasMappingRepository=fieldAliasMappingRepository;
    this.patientRepository=patientRepository;
    this.doctorRepository=doctorRepository;

}
    @Override
    @Transactional
    public void process(RawDataEvent rawDataEvent){
        String path  = rawDataEvent.getStoragePath();
        Path filePath = Paths.get(path);
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))){
            //the reader object will be used to call .readline method which reads only 1 line ,aka the header of our csv
            String headerLine = reader.readLine();
            //now i will use .split to get the headers in format and store in an array
            String[] headers=headerLine.split(",");
            //implement/declaring the mappinglaw
            Map<String,String> aliasMapping = getAliasAndEntityField(getFileType());
            Map<Integer,String> columnIndexToField = new HashMap<>();
            for (int i=0;i<headers.length;i++){
                //now loop through the headers array and we are basicallt extracting headers of each column and basically like putting a index and kind of storing them in hashmap<<key-value pair ofcourse>>
                String rawHeader= headers[i]; //now rawHeader contains say headers[0] =ExternalId
                String normalizedHeader = rawHeader.toLowerCase().trim(); // externalid
                String field=aliasMapping.get(normalizedHeader); //k=externalid,.get(key)=returns value at key,aka externalid,which is externalId in our "dictionary"
                if(field!=null){
                    columnIndexToField.put(i,field);
                }else{
                    System.out.println("Unknown header: " + rawHeader + "will be ignored");
                }
            }

            //now reading data part cause like i said .readline reads till first /n
            String dataLine;
            int rowNumber=1;
            while((dataLine=reader.readLine())!=null){
                rowNumber++;
                String[] data =dataLine.split(",");
                try{
                    PrescriptionDTO dto = mapDataToDTO(data,columnIndexToField);
                    validateDto(dto);
                    Patient patient=patientRepository.findByExternalId(dto.getPatientExternalId());
                    Doctor doctor=doctorRepository.findByExternalId(dto.getDoctorExternalId());
                    if(patient==null||doctor==null){
                        throw new IllegalArgumentException("Invalid patient or doctor external id");
                    }
                    Optional<ClinicalEncounter> existingEncounter = clinicalEncounterRepository.findByDoctorAndPatientAndDate(doctor,patient,dto.getDate());
                    ClinicalEncounter encounterToUse;
                    if(existingEncounter.isPresent()){
                        encounterToUse=existingEncounter.get();
                    }else{
                        ClinicalEncounter newEncounter = new ClinicalEncounter();
                        newEncounter.setDoctor(doctor);
                        newEncounter.setPatient(patient);
                        newEncounter.setDate(dto.getDate());
                        encounterToUse=clinicalEncounterRepository.save(newEncounter);
                    }
                    Prescription prescription = new Prescription();
                    prescription.setExternalId(dto.getExternalId());
                    prescription.setMedicationName(dto.getMedicationName());
                    prescription.setNotes(dto.getNotes());
                    prescription.setEncounter(encounterToUse);
                    prescriptionRepository.save(prescription);

                    
                }catch (Exception e){
                    System.err.println("Error processing row " + rowNumber + ". Skipping row.");
                    e.printStackTrace(); // Log the full stack trace for debugging
                }
            }


        }catch(Exception e){
            System.err.println("Error processing file : " + e.getMessage());
            throw new RuntimeException();
        }
    }
    @Override
    public String getFileType(){
        return "Prescription";
    }

    private Map<String,String> getAliasAndEntityField(String entityType){
        List<FieldAliasMapping> mappings = fieldAliasMappingRepository.findByEntityType(entityType);
        return mappings.stream()
                .collect(Collectors.toMap(FieldAliasMapping::getAliasField,FieldAliasMapping::getMappedField));
    }

    private PrescriptionDTO mapDataToDTO(String[] data,Map<Integer,String> plan){
        PrescriptionDTO dto  = new PrescriptionDTO();
        //loop throguh each plan entry returned as a set of entries and .entry kind of like makes them 1 1 1 ,,like for each loop kind of thing
        for(Map.Entry<Integer,String> entry:plan.entrySet()){
            Integer index=entry.getKey();
            String fieldName = entry.getValue();
            if(index<data.length){
                String value =data[index].trim();
                switch(fieldName){
                    case "externalId":
                        dto.setExternalId(value);
                        break;
                    case "patientExternalId":
                        dto.setPatientExternalId(value);
                        break;
                    case "doctorExternalId":
                        dto.setDoctorExternalId(value);
                        break;
                    case "medicationName":
                        dto.setMedicationName(value);
                        break;
                    case "notes":
                        dto.setNotes(value);
                        break;
                    case "date":
                        dto.setDate(LocalDate.parse(value));
                        break;
                    default:
                        System.out.println("unknown field");
                        break;
                }
            }
        }
        return dto;
    }

    private void validateDto(PrescriptionDTO dto) {
        if (dto.getExternalId() == null || dto.getExternalId().isEmpty()) {
            throw new IllegalArgumentException("External id is missing");
        }
        if(dto.getPatientExternalId()==null||dto.getPatientExternalId().isEmpty()){
            throw new IllegalArgumentException("Patient External id is missing");
        }
        if(dto.getDoctorExternalId()==null||dto.getDoctorExternalId().isEmpty()){
            throw new IllegalArgumentException("Doctor External id is missing ");
        }
        if(dto.getMedicationName()==null||dto.getMedicationName().isEmpty()){
            throw new IllegalArgumentException("Medication name is empty");
        }
        if(dto.getNotes()==null||dto.getNotes().isEmpty()){
            throw new IllegalArgumentException("Notes are missing");
        }
        if(dto.getDate()==null){
            throw new IllegalArgumentException("Date is missing");
        }

    }
}
