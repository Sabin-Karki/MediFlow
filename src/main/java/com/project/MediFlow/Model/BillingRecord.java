package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BillingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private  Long amount;

    @ManyToOne
    @JoinColumn(name = "encounterId")
    private  ClincialEncounter encounter;
}
