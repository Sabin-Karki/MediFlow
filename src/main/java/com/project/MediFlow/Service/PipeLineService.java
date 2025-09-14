package com.project.MediFlow.Service;

import com.project.MediFlow.Model.RawDataEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface PipeLineService {

     void processPipeLine(Long id);
    public RawDataEvent saveRawDataEvent(MultipartFile file,String fileType);
}

