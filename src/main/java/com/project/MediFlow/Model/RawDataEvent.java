package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RawDataEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  String originalFileName;

    private LocalDateTime recievedTimeStamp;
    private  LocalDateTime completeddTimeStamp;  // completed meaning


    @Enumerated(EnumType.STRING)
    private JobStatus status;


}

enum JobStatus{
    RECEIVED,
    PROCESSING,
    COMPLETED,
    FAILED
}