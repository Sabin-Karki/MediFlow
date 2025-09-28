package com.project.MediFlow.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @Column(unique=true)
    private  String externalId;
    private LocalDate date;


    @ManyToOne
    @JoinColumn(name = "encounterId")
    private  ClincialEncounter encounter;
}
