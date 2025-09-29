package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//what could prescription even have
// id ofcourse,  name ? prescriptionName ? notes ? how much to take and stuff
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @Column(unique = true)
    private String externalId;

    private String medicationName;
    private  String notes;

    @ManyToOne
    @JoinColumn(name="encounterId")
    private ClinicalEncounter encounter;
}
