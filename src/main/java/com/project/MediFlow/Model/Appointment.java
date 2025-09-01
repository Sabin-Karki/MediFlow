package com.project.MediFlow.Model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  Long date;

    @ManyToOne
    @JoinColumn(name = "encounterId")
    private  ClincialEncounter encounter;
}
