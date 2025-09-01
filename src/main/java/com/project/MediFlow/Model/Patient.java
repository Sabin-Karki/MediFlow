package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
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
