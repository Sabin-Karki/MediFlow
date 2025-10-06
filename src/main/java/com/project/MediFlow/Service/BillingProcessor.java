package com.project.MediFlow.Service;

import com.project.MediFlow.DTO.BillingRecordDTO;
import com.project.MediFlow.Model.*;
import com.project.MediFlow.Repository.BillingRepository;
import com.project.MediFlow.Repository.ClinicalEncounterRepository;
import com.project.MediFlow.Repository.FieldAliasMappingRepository;
import com.project.MediFlow.Repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.Buffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillingProcessor implements FileProcessor {
private  final FieldAliasMappingRepository fieldAliasMappingRepository;
private  final BillingRepository billingRepository;
private final PatientRepository patientRepository;
private final ClinicalEncounterRepository clinicalEncounterRepository;
public BillingProcessor(FieldAliasMappingRepository fieldAliasMappingRepository,BillingRepository billingRepository,PatientRepository patientRepository,ClinicalEncounterRepository clinicalEncounterRepository){
    this.fieldAliasMappingRepository=fieldAliasMappingRepository;
    this.billingRepository=billingRepository;
    this.patientRepository=patientRepository;
    this.clinicalEncounterRepository=clinicalEncounterRepository;
}
    @Override
    @Transactional
    public void process(RawDataEvent rawDataEvent){
        String path = rawDataEvent.getStoragePath();
        Path filePath = Paths.get(path);
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))){
            String headerLine = reader.readLine();
            String[] headers = headerLine.split(",");
            Map<String ,String> aliasMapping =getAliasAndEntityField(getFileType());

            Map<Integer,String> columnIndexToField = new HashMap<>();
            for(int i=0;i<headers.length;i++){
                String rawHeader = headers[i];
                String normalizedHeader = rawHeader.toLowerCase().trim();
                String field = aliasMapping.get(normalizedHeader);
                if(field!=null){
                    columnIndexToField.put(i,field);
                }else{
                    throw new IllegalArgumentException("Unknown header : " + rawHeader);

                }

                String dataLine;
                int rowNumber = 1;
                while((dataLine=reader.readLine())!=null){
                    String [] data = dataLine.split(",");
                    BillingRecordDTO billingRecordDTO = mapDataToDTO(data,columnIndexToField);
                    validateDto(billingRecordDTO);
                    if(billingRepository.existsByExternalId(billingRecordDTO.getExternalId())){
                        continue; //skip the row
                    }
                    Patient patient = patientRepository.findByExternalId(billingRecordDTO.getPatientExternalId());
                    if(patient==null){
                        throw new IllegalArgumentException("Invalid patient external ID");
                    }
                    Optional<ClinicalEncounter> existingEncounter = clinicalEncounterRepository.findByPatientAndDate(patient,billingRecordDTO.getBillingDate());
                    ClinicalEncounter encounterToUse;
                    if(existingEncounter.isPresent()){
                        encounterToUse=existingEncounter.get();
                        System.out.println("Using clinical encounter with ID: " + encounterToUse.getId());
                    }else{
                        ClinicalEncounter newEncounter = new ClinicalEncounter();
                        newEncounter.setPatient(patient);
                        newEncounter.setDate(billingRecordDTO.getBillingDate());
                        encounterToUse=clinicalEncounterRepository.save(newEncounter);
                    }
                   BillingRecord billingRecord= new BillingRecord();
                    billingRecord.setExternalId(billingRecordDTO.getExternalId());
                    billingRecord.setAmount(Long.valueOf(billingRecordDTO.getAmount()));
                    billingRecord.setEncounter(encounterToUse);
                    billingRepository.save(billingRecord);

                }
            }

        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public String getFileType(){
        return "BillingRecord";
    }
    private  Map<String,String> getAliasAndEntityField(String entityType){
        List<FieldAliasMapping> mappings = fieldAliasMappingRepository.findByEntityType(entityType);
    return mappings.stream()
            .collect(Collectors.toMap(FieldAliasMapping::getAliasField,FieldAliasMapping::getMappedField));
}
//the plan is the map which stores the integer and string fields
private BillingRecordDTO mapDataToDTO(String[] data, Map<Integer,String > plan){
    BillingRecordDTO dto = new BillingRecordDTO();
    for(Map.Entry<Integer,String> entry:plan.entrySet()){
        Integer index = entry.getKey();
        String field = entry.getValue();
        if(index<data.length){
            String value = data[index].trim();
            switch(field){
                case "externalId":
                    dto.setExternalId(value);
                    break;
                case "patientExternalId":
                    dto.setPatientExternalId(value);
                    break;
                case "amount":
                    dto.setAmount(value);
                    break;
                case "billingDate":
                    dto.setBillingDate(LocalDate.parse(value));
                    break;
                default:
                    System.out.println("Unknown field : " + field + " will be ignored ");
                    break;
            }
        }
    }
    return dto;
}
private void validateDto(BillingRecordDTO billingRecordDTO){
    if(billingRecordDTO.getExternalId()==null||billingRecordDTO.getExternalId().isEmpty()){
        System.err.println("External ID is missing");
        throw new IllegalArgumentException("External ID is missing");
    }
    if(billingRecordDTO.getPatientExternalId()==null||billingRecordDTO.getPatientExternalId().isEmpty()){
        System.err.println("Patient External ID is missing");
        throw  new IllegalArgumentException("Patient External Id is missing");
    }
    if(billingRecordDTO.getAmount()==null||billingRecordDTO.getAmount().isEmpty()){
        System.err.println("Amount is missing");
        throw new IllegalArgumentException("Amount is missing");
    }
    if(billingRecordDTO.getBillingDate()==null){
        System.err.println("Billing Date is missing");
        throw new IllegalArgumentException("Date is missing");
    }
}
}
