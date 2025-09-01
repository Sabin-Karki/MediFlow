package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private  Long id;

    private String test;
    private  String result;
    private String unit ;

    // create a encounter fk here / / so this has to be the many part in manytoone relationship
    @ManyToOne
    @JoinColumn(name = "encounterId")
    private ClincialEncounter encounter;


}
