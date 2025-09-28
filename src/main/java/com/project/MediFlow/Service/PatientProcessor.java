package com.project.MediFlow.Service;

import com.project.MediFlow.DTO.PatientDTO;
import com.project.MediFlow.Model.FieldAliasMapping;
import com.project.MediFlow.Model.Patient;
import com.project.MediFlow.Model.RawDataEvent;
import com.project.MediFlow.Repository.FieldAliasMappingRepository;
import com.project.MediFlow.Repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PatientProcessor implements  FileProcessor{

    private  final FieldAliasMappingRepository fieldAliasMappingRepository;
    private  final PatientRepository patientRepository;

    public PatientProcessor(FieldAliasMappingRepository fieldAliasMappingRepository, PatientRepository patientRepository){
        this.fieldAliasMappingRepository = fieldAliasMappingRepository;
        this.patientRepository=patientRepository;
    }

    @Override
    public void process(RawDataEvent rawDataEvent) {
     String path = rawDataEvent.getStoragePath();
        Path filePath = Paths.get(path);
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
         String Line = reader.readLine(); // the bufferedreader above and filereader read the file say line by line Fname,Lname, /n Tommy,Hill,Line variable now stores these as string
         String[] headerLine = Line.split(",");

         //now the heaaderLine variable has for eg : ["Fname","Lname"]
            Map<String,String> aliasMapping = getAliasMappingForEntityType(getFileType());
          //now aliasMapping variable has the entityType of the file uploaded?
          
          //create a mapping plan so that we can map the values to accurate field cause we do not know the order of the .csv header which will be sent as it will be different each time,and this is also for alias - > mapped accurate field

            Map<Integer,String> columnIndextoFieldName = new HashMap<>();
            //headerLine = ["fName","LName","DateofBirth"] 0 1 2
            for(int i=0;i< headerLine.length;i++){
                String rawHeader = headerLine[i];
                String normalizedHeader = rawHeader.toLowerCase().trim();
                // now i get the accurate field name using the aliasMapping (dictionary) , normalizedHeader= fname,now the goal is for accurate field Name which is firstName for the dto
                String field = aliasMapping.get(normalizedHeader);
                if(field != null){
                    columnIndextoFieldName.put(i,field);
                }else{
                    System.out.println(" Unknown header : " + rawHeader + " will be ignored ");
                }

            }
            // the first readLine() reads only from the 1st line remember that  ,so i need to read the data too
            String dataLine;
            int rowNumber = 1;
            while((dataLine=reader.readLine())!=null){
                rowNumber++ ;
                String[] data = dataLine.split(",");
                try{
                    PatientDTO dto = mapDatatoDTO(data,columnIndextoFieldName);
                    //validate the dto
                    validateDTO(dto);
                    Boolean patientExists  = patientRepository.existsByFirstAndLastName(dto.getFirstName(),dto.getLastName(),dto.getDOB());
                    if (patientExists){
                        System.err.println(" Duplicate Data Found  in Row " + rowNumber);
                        continue; //skip this row

                    }
                    //create and save patient
                    Patient newPatient = new Patient();
                    newPatient.setExternalId(dto.getExternalId());
                    newPatient.setFirstName(dto.getFirstName());
                    newPatient.setLastName(dto.getLastName());
                    newPatient.setDOB(dto.getDOB());
                    patientRepository.save(newPatient);

                }catch (Exception e){

                }
            }

        }catch (Exception e){

        }
    }

    @Override
    public String getFileType() {
        return "Patient";
    }

    private Map<String,String > getAliasMappingForEntityType(String entityType){
        List<FieldAliasMapping> mapping = FieldAliasMappingRepository.findByEntityType(entityType);
        return mapping.stream()
                .collect(Collectors.toMap(
                FieldAliasMapping::getAliasField,
                FieldAliasMapping::getMappedField
                ));


    }

    private PatientDTO mapDatatoDTO(String[] data , Map<Integer,String> plan){
      PatientDTO dto = new PatientDTO();
      //loop through the hashmap which has integer and respective field name and get the key value pair
      for(Map.Entry<Integer,String> entry:plan.entrySet()){
          Integer columnIndex = entry.getKey();
          String fieldName = entry.getValue();
          if(columnIndex<data.length){
              String value = data[columnIndex].trim();
              switch (fieldName){
                  case "externalId":
                      dto.setExternalId(value);
                      break;

                  case  "firstName":
                      dto.setFirstName(value);
                      break;
                  case  "lastName":
                      dto.setLastName(value);
                      break;
                  case "DOB":
                      dto.setDOB(LocalDate.parse(value));
                      break;
              }
          }
      }
      return  dto;

    }

    private void validateDTO(PatientDTO dto) {
        if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is missing or empty.");
        }
        if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name is missing or empty.");
        }
        if (dto.getDOB() == null) {
            throw new IllegalArgumentException("Date of birth is missing or empty.");
        }
        // You could add more complex validation here, like checking date format
    }
}

