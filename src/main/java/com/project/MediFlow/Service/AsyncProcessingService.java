package com.project.MediFlow.Service;


import com.project.MediFlow.Model.JobStatus;
import com.project.MediFlow.Model.RawDataEvent;
import com.project.MediFlow.Repository.RawDataEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AsyncProcessingService {
private final RawDataEventRepository rawDataEventRepository;
private final Map<String,FileProcessor> processors;
public AsyncProcessingService(RawDataEventRepository rawDataEventRepository, List<FileProcessor> processors){
    this.rawDataEventRepository=rawDataEventRepository;
    this.processors=processors.stream()
            .collect(Collectors.toMap(FileProcessor::getFileType, Function.identity()));
}

@Async
@Transactional
    public void processPipeLine(Long id){
    Optional<RawDataEvent> optionalEvent = rawDataEventRepository.findById(id);
    if(!optionalEvent.isPresent()){
        throw new IllegalArgumentException("Invalid RawDataEvent id ; " + id);
    }

    RawDataEvent rawDataEvent = optionalEvent.get();
    try{
        rawDataEvent.setJobStatus(JobStatus.PROCESSING);
        rawDataEventRepository.save(rawDataEvent);

        String fileType = rawDataEvent.getFileType();
        FileProcessor processor = processors.get(fileType);
        if(processor==null){
            throw new IllegalArgumentException("No processor found  for file type :"  + fileType);
        }
        processor.process(rawDataEvent);
        rawDataEvent.setJobStatus(JobStatus.COMPLETED);
        rawDataEvent.setCompletedTimeStamp(LocalDateTime.now());
        rawDataEventRepository.save(rawDataEvent);
    }catch (Exception e){
        System.err.println("Error processing RawDataEvent id : " + id);
        e.printStackTrace();
        RawDataEvent failedEvent = rawDataEventRepository.findById(id).get();
        failedEvent.setJobStatus(JobStatus.FAILED);
        rawDataEventRepository.save(failedEvent);

    }
}

}
