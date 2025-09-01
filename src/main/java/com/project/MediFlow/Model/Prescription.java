package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.Data;


//what could prescription even have
// id ofcourse,  name ? prescriptionName ? notes ? how much to take and stuff
@Data
@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String medicationName;
    private  String notes;

    @ManyToOne
    @JoinColumn(name="encounterId")
    private  ClincialEncounter encounter;
}
