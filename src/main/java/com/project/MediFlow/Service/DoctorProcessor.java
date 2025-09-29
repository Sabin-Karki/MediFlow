package com.project.MediFlow.Service;

import com.project.MediFlow.DTO.DoctorDTO;
import com.project.MediFlow.Model.Doctor;
import com.project.MediFlow.Model.FieldAliasMapping;
import com.project.MediFlow.Model.RawDataEvent;
import com.project.MediFlow.Repository.DoctorRepository;
import com.project.MediFlow.Repository.FieldAliasMappingRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorProcessor implements  FileProcessor{
// dictionary which has alias field and entity field and entityType fname->firstName->Doctor
    private FieldAliasMappingRepository fieldAliasMappingRepository;
    private DoctorRepository doctorRepository;

    public DoctorProcessor(FieldAliasMappingRepository fieldAliasMappingRepository, DoctorRepository doctorRepository){
        this.fieldAliasMappingRepository = fieldAliasMappingRepository;
        this.doctorRepository=doctorRepository;
    }

    @Override
    public void process(RawDataEvent rawDataEvent) {
        String storagePath = rawDataEvent.getStoragePath();
        //now representing the file path in Path object
        Path filePath = Paths.get(storagePath);
        //now that i have the file path represented in path object i could totally use java class to read the file inside the path
        //use bufferedreader ofcourse to read line by line and not character by character
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))){
           Map<String,String> aliasMapping = getAliasAndEntityField(getFileType()); // k-v pair which holds the alias field and the entity field,LNAME->lastName ,the goal really is that the header say from alias lname is not like converted to lastName but instead its normalized properly and then check the dictionary,find the match with lastName and boom ,the entity header becomes lastName

           //read first line aka the headers
            String headerLine = reader.readLine();  //reads till /n ,the first line basically
            String[] header = headerLine.split(",");

            // implementing the mapping "laws"
            Map<Integer,String> columnIndextoField = new HashMap<>();
            for (int i=0;i< header.length;i++){
                String rawHeader = header[i];
                String normalizedHeader = rawHeader.toLowerCase().trim();
                //use aliasmapping.get(key) to get the value <map feature >  and store the value of that key in field,fname k ->> firstName value to field..the whole vpoint of mapping class to store the k - v pair ,,the older map btw of string ,string
                String field = aliasMapping.get(normalizedHeader); // so field = firstName <assuming its at 0 index btw >
                if (field!=null){
                    columnIndextoField.put(i,field);
                }else{
                    System.out.println(" Unknown header : "  + rawHeader  + " will be ignored ");
                }
            }

            //the reading of header line 0 row number is done now time to read the data
            String dataLine;
            int rowNumber = 1;
            while(( dataLine=reader.readLine())!=null){
                rowNumber ++;
                // read the 1st row of dataline
                String[] data = dataLine.split(",");
                try{
                    DoctorDTO dto = mapDatatoDto(data,columnIndextoField);
                    validateDTO(dto);
                    //convert date to localdate
                    Boolean doctorExists = doctorRepository.existsByFirstNameAndLastName(dto.getFirstName(),dto.getLastName(),dto.getDOB());
                    if(doctorExists){
                        continue; // skip row 1
                    }

                    Doctor newDoctor = new Doctor();
                    newDoctor.setExternalId(dto.getExternalId());
                    newDoctor.setFirstName(dto.getFirstName());
                    newDoctor.setLastName(dto.getLastName());
                    newDoctor.setDOB(dto.getDOB());

                    doctorRepository.save(newDoctor);

                }catch (Exception e){
                    System.err.println("Error");
                }

            }


            }catch(IOException e){
            throw new IllegalArgumentException();

        }
    }

    @Override
    public String getFileType(){
        return "Doctor";
    }


    //return type is map ofcoruse
    private Map<String,String> getAliasAndEntityField(String entityType){
        //return the List fieldaliasmapping of type Doctor and make mapping point to the list
        List<FieldAliasMapping> mapping = fieldAliasMappingRepository.findByEntityType(entityType);
        //now turn them to map with key and value pair of string and string and stored in the map collection,,the alias field and entity field is then stored
        return mapping.stream()
                .collect(Collectors.toMap(FieldAliasMapping::getAliasField,
                        FieldAliasMapping::getMappedField));
    }

    private  DoctorDTO mapDatatoDto(String[] data , Map<Integer,String> plan){
        DoctorDTO dto = new DoctorDTO();
        //data say 1st row as in 0 1 ,1 row,
        // data = ["henry" , "frauk" ,2003-03-12]
        //so my goal is to make that the data say form index i mathces the index i field name say for here firstName and the dto.setsFirstName(data)
        //iteratin time,what ths does is iterate over the plan map and get the key -value pair
        for(Map.Entry<Integer,String> entry:plan.entrySet()){
            Integer index = entry.getKey();
            String fieldName = entry.getValue();
            if(index< data.length){
                String value = data[index].trim();  // value ->data[0] ->column btw not row ,so 0 say its externalid field then retrieves the data of it and value ->Hospital-Id-05
                switch (fieldName){
                    case "externalId":
                        dto.setExternalId(value);
                        break;
                    case "firstName":
                        dto.setFirstName(value);
                        break;

                    case "lastName":
                        dto.setLastName(value);
                        break;

                    case "DOB":
                        dto.setDOB(LocalDate.parse(value));
                }
            }
        }
        return dto;
    }

    private  void validateDTO(DoctorDTO dto){
        if(dto.getExternalId()==null || dto.getExternalId().isEmpty()){
            throw new IllegalArgumentException("External Id is missing");
        }
        if(dto.getFirstName()==null||dto.getFirstName().isEmpty()){
            throw new IllegalArgumentException("FirstName is missing");
        }
        if(dto.getLastName()==null||dto.getLastName().isEmpty()){
            throw new IllegalArgumentException("LastName is missing ");

        }
        if(dto.getDOB()==null){
            throw new IllegalArgumentException("Date Of Birth is missing");
        }
    }
}
