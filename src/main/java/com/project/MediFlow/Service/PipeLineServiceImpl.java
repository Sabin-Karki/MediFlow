package com.project.MediFlow.Service;

import com.project.MediFlow.Model.JobStatus;
import com.project.MediFlow.Model.RawDataEvent;
import com.project.MediFlow.Repository.RawDataEventRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PipeLineServiceImpl implements  PipeLineService {

    private final RawDataEventRepository rawDataEventRepository;
    private final String FILE_UPLOADS = "/home/kantwasright/Documents/uploads";

    public PipeLineServiceImpl(RawDataEventRepository rawDataEventRepository) {
        this.rawDataEventRepository = rawDataEventRepository;
    }

    public RawDataEvent saveRawDataEvent(MultipartFile file, String fileType) throws IllegalArgumentException {
        String fileContent = file.getOriginalFilename();
        if (fileContent == null || !fileContent.endsWith("csv")) {
            throw new IllegalArgumentException("File is empty");

        } else {
            // now i need to create a unqiue id for the file <uuid>,save it to the file path and then save it to the database
            String extension = ".csv";
            String uniqueFileName = UUID.randomUUID().toString() + extension;
            // <create  a path for the file to be stored in >
            Path filePath = Paths.get(FILE_UPLOADS, uniqueFileName);
            //now save the file to the path
            try {
                file.transferTo(filePath);
                //now time to create RawDataEvent Object
                RawDataEvent rawDataEvent = new RawDataEvent(
                        file.getOriginalFilename(),
                        filePath.toString(),
                        LocalDateTime.now(),
                        JobStatus.RECEIVED
                );


                RawDataEvent savedRawDataEvent = rawDataEventRepository.save(rawDataEvent);

                this.processPipeLine(savedRawDataEvent.getId());
                return savedRawDataEvent;
            } catch (java.io.IOException e) {
                System.err.println("Failed to save file" + e.getMessage());
                throw new IllegalArgumentException("Failed to save file");
            }

        }
    }



    @Async
    public void processPipeLine(Long id) {
        //monitor the database of raw data event and check the status,when it sees the status it changes received to processing,it then gets the file path and processes it,determins the type of file it is and then process it accordingly //after processing change the status code to completed//i want to use async processing not scheduled !!!
        Optional<RawDataEvent> optionalEvent = rawDataEventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            RawDataEvent rawDataEvent = optionalEvent.get();
            //update status to processing after retriving the object
            try {
                rawDataEvent.setStatus(JobStatus.PROCESSING);
                rawDataEventRepository.save(rawDataEvent);
                if(rawDataEvent.getOriginalFileName().endsWith("csv")){
                   processPatient(rawDataEvent);
                }
            } catch (Exception e) {
                System.err.println("Failed to update status" + e.getMessage());

            }


        }
    }

    public void processPatient(RawDataEvent rawDataEvent){
        //i need to see status of processing
        if(rawDataEvent.getStatus()==JobStatus.PROCESSING){
            // read the file
            
        }

    }

}




