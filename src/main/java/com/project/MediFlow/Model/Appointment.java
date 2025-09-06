package com.project.MediFlow.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  Long date;

    @ManyToOne
    @JoinColumn(name = "encounterId")
    private  ClincialEncounter encounter;
}
