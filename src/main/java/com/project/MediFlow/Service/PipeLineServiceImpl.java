package com.project.MediFlow.Service;

import com.project.MediFlow.Model.JobStatus;
import com.project.MediFlow.Model.RawDataEvent;
import com.project.MediFlow.Repository.RawDataEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class PipeLineServiceImpl implements  PipeLineService {

    private final RawDataEventRepository rawDataEventRepository;
  private  final AsyncProcessingService asyncProcessingService;
    private final String FILE_UPLOADS = "/home/kantwasright/Documents/uploads";


    //so i really have intitally the list of instance of fileprocessor in list basically
    //then while creating map im creating a collection of key-value of filetype->instance/implementation of fileprocessor,eg-Patient-PatientProcesoor,basic storage/collectiopn whatver.and then later i will retrieve the correct instance of the filetype using method .get(key) where key is filetype
    public PipeLineServiceImpl(RawDataEventRepository rawDataEventRepository,AsyncProcessingService asyncProcessingService) {
        this.rawDataEventRepository = rawDataEventRepository;
        this.asyncProcessingService=asyncProcessingService;


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
                        fileType,
                        LocalDateTime.now(),
                        JobStatus.RECEIVED
                );


                RawDataEvent savedRawDataEvent = rawDataEventRepository.save(rawDataEvent);

                asyncProcessingService.processPipeLine(savedRawDataEvent.getId());
                return savedRawDataEvent;
            } catch (java.io.IOException e) {
                System.err.println("Failed to save file" + e.getMessage());
                throw new IllegalArgumentException("Failed to save file");
            }

        }
    }
}




