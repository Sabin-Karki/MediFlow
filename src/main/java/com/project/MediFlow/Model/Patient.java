package com.project.MediFlow.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private  String firstName;
    private String lastName;
    private  Long DOB;

    @OneToMany(mappedBy = "patient")
    private List<ClincialEncounter> encountersList;


}
