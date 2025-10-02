package com.project.MediFlow.Service;

import com.project.MediFlow.DTO.LabResultDTO;
import com.project.MediFlow.Model.*;
import com.project.MediFlow.Repository.*;
import org.springframework.stereotype.Service;

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
public class LabProcessor implements  FileProcessor {
    private final FieldAliasMappingRepository fieldAliasMappingRepository;
    private final ClinicalEncounterRepository clinicalEncounterRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final LabResultRepository labResultRepository;

    public LabProcessor(FieldAliasMappingRepository fieldAliasMappingRepository, ClinicalEncounterRepository clinicalEncounterRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, LabResultRepository labResultRepository){
        this.fieldAliasMappingRepository=fieldAliasMappingRepository;
        this.clinicalEncounterRepository=clinicalEncounterRepository;
        this.doctorRepository=doctorRepository;
        this.patientRepository=patientRepository;
        this.labResultRepository=labResultRepository;
    }

    @Override
    public void process(RawDataEvent rawDataEvent){
      String filePath = rawDataEvent.getStoragePath();
        Path path = Paths.get(filePath);
        try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))){
            String line = reader.readLine();
            String[] header = line.split(",");
            //implement the mapping law , like declare it
            Map<String,String> aliasMapping = getAliasAndEntityField(getFileType());

            Map<Integer,String> columnIndexToField = new HashMap<>(); //just trying to map the index of column to accurate field headers
            for(int i =0;i<header.length;i++){
                String rawHeader = header[i];
                String normalizedHeader = rawHeader.toLowerCase().trim();
                String field = aliasMapping.get(normalizedHeader);//nh is the key,so doing .get(key) to get the value of that key stored in Map
                if(field!=null){
                    columnIndexToField.put(i,field);
                }else{
                    System.out.println(" Unknown header : " + rawHeader + " will be ignored ");
                }
            }

            //data reading part
            String dataLine;
            int rowNumber = 1;
            while((dataLine=reader.readLine())!=null){
                rowNumber++;
                String[] data = dataLine.split(",");
                try{
                    LabResultDTO labResultDTO = mapDataToDTO(data,columnIndexToField);
                    validateDTO(labResultDTO);
                    if(labResultRepository.existsByExternalId(labResultDTO.getExternalId())){
                        continue;//skip the duplicate row
                    }
                    Doctor doctor = doctorRepository.findByExternalId(labResultDTO.getDoctorExternalId());
                    Patient patient=patientRepository.findByExternalId(labResultDTO.getPatientExternalId());
                    LocalDate date = labResultDTO.getTestDate();
                    if(doctor==null||patient==null){
                        System.err.println("Doctor or Patient external id is missing .");
                    }
                    Optional<ClinicalEncounter> existingEncounter = clinicalEncounterRepository.findByDoctorAndPatientAndDate(doctor,patient,date);
                    ClinicalEncounter encounterToUse;
                    if(existingEncounter.isPresent()){
                        encounterToUse=existingEncounter.get();

                    }else{
                        ClinicalEncounter newEncounter = new ClinicalEncounter();
                        newEncounter.setDoctor(doctor);
                        newEncounter.setPatient(patient);
                        newEncounter.setDate(date);
                        encounterToUse=clinicalEncounterRepository.save(newEncounter);
                    }


                    LabResult newLabResult = new LabResult();
                    newLabResult.setExternalId(labResultDTO.getExternalId());
                    newLabResult.setTest(labResultDTO.getTestName());
                    newLabResult.setUnit(labResultDTO.getUnit());
                    newLabResult.setResult(labResultDTO.getResultValue());
                    newLabResult.setEncounter(encounterToUse);
                    labResultRepository.save(newLabResult);

                }catch (Exception e){
                    throw new RuntimeException(e.getMessage());
                }
            }
        }catch(Exception e){
        throw new RuntimeException("Error processing labresult file" + e.getMessage());
        }

    }

    @Override
    public String getFileType(){
        return "LabResult";
    }

    private Map<String,String> getAliasAndEntityField(String entityType){
           List<FieldAliasMapping> mappings = fieldAliasMappingRepository.findByEntityType(entityType);
           return mappings.stream()
                   .collect(Collectors.toMap(FieldAliasMapping::getAliasField,FieldAliasMapping::getMappedField));
    }

    private LabResultDTO mapDataToDTO(String[] data , Map<Integer,String> plan){
        LabResultDTO labResultDTO = new LabResultDTO();
        for(Map.Entry<Integer,String> entry:plan.entrySet()){
            Integer index = entry.getKey();
            String fieldName = entry.getValue();
            if(index<data.length){
                String value = data[index].trim();
                switch(fieldName){
                    case "externalId":
                        labResultDTO.setExternalId(value);
                        break;
                    case "patientExternalId":
                        labResultDTO.setPatientExternalId(value);
                        break;
                    case "doctorExternalId":
                        labResultDTO.setDoctorExternalId(value);
                        break;
                    case "testName":
                        labResultDTO.setTestName(value);
                        break;
                    case "resultValue":
                        labResultDTO.setResultValue(value);
                        break;
                    case "unit":
                        labResultDTO.setUnit(value);
                        break;
                    case "testDate":
                        labResultDTO.setTestDate(LocalDate.parse(value));
                    default :
                        throw new IllegalArgumentException();

                }
            }
        }
        return labResultDTO;
    }
    private void validateDTO(LabResultDTO labResultDTO){
      if(labResultDTO.getExternalId()==null||labResultDTO.getExternalId().isEmpty()){
          System.err.println("External ID is missing");
      }
      if(labResultDTO.getPatientExternalId()==null||labResultDTO.getPatientExternalId().isEmpty()){
          System.err.println("Patient id is missing");
      }
      if(labResultDTO.getDoctorExternalId()==null||labResultDTO.getDoctorExternalId().isEmpty()){
          System.err.println("Doctor id is missing");
      }
      if(labResultDTO.getTestName()==null||labResultDTO.getTestName().isEmpty()){
          System.err.println("Test is missing");
      }
      if(labResultDTO.getResultValue()==null||labResultDTO.getResultValue().isEmpty()){
          System.err.println("Result is empty ");
      }
      if(labResultDTO.getUnit()==null||labResultDTO.getUnit().isEmpty()){
          System.err.println("Unit is missing " );
      }
    }
}
