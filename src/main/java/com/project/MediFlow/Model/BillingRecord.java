package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class BillingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  Long amount;

    @ManyToOne
    @JoinColumn(name = "encounterId")
    private  ClincialEncounter encounter;
}
