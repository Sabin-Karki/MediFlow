package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private  Long externalId;

    private  String firstName;
    private String lastName;
    private LocalDate DOB;

    @OneToMany(mappedBy = "doctor")
    private List<ClincialEncounter> encounterList;
}
