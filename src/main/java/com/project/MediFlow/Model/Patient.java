package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private  String firstName;
    private String lastName;
    private LocalDate DOB;

    @OneToMany(mappedBy = "patient")
    private List<ClincialEncounter> encountersList;


}
