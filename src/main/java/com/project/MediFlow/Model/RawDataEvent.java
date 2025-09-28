package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class RawDataEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  String originalFileName;
    private String storagePath;
    private String fileType;

    private LocalDateTime receivedTimeStamp;
    private  LocalDateTime completedTimeStamp;  // completed meaning

    public RawDataEvent(String originalFileName,String storagePath,String fileType,LocalDateTime receivedTimeStamp,JobStatus status){
        this.originalFileName = originalFileName;
        this.storagePath = storagePath;
        this.fileType=fileType;
        this.receivedTimeStamp = receivedTimeStamp;
        this.status = status;
        this.completedTimeStamp=null;
    }

    @Enumerated(EnumType.STRING)
    private JobStatus status;



}
