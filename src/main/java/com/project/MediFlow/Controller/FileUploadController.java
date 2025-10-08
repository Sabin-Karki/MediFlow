package com.project.MediFlow.Controller;

import com.project.MediFlow.Model.RawDataEvent;
import com.project.MediFlow.Service.PipeLineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//this class is to handle file upload,,i know exactly to use multipartfile to handle the upload and use requestparam so i can "extract" the parameter and get the file object so i can then manipulate it ,RestController is to handle the request and response and it does not even need a view to send a response



@RestController
@RequestMapping("/file/ingest")
public class FileUploadController {
//    private final PipeLineService ;

    public final PipeLineService pipeLineService;

    public FileUploadController(PipeLineService pipeLineService) {
        this.pipeLineService = pipeLineService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(name = "file") MultipartFile file, @RequestParam String fileType) {
          try{
              RawDataEvent rawDataEvent = pipeLineService.saveRawDataEvent(file,fileType);
              return ResponseEntity.status(HttpStatus.ACCEPTED).body("File uploaded successfully : " + rawDataEvent.getId());
          }catch(IllegalArgumentException e){
              return ResponseEntity.badRequest().body("File upload failed : " + e.getMessage() );
          }catch (Exception e){
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured during file upload : " + e.getMessage());
          }
    }
}
