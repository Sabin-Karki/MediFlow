package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ClincialEncounter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @ManyToOne
    @JoinColumn(name = "patientId") // creates a foreign key column
    private Patient patient;  // patient holds the realtionship

    @ManyToOne
    @JoinColumn(name = "doctorId")
    private  Doctor doctor;

    @OneToMany(mappedBy = "encounter")
    private List<Appointment>  appointmentList;

    @OneToMany(mappedBy = "encounter")
    private List<BillingRecord> billingRecordList;

    @OneToMany(mappedBy = "encounter")
    private  List<LabResult> labResultList;

    @OneToMany(mappedBy = "encounter")
    private  List<Prescription> prescriptionList;

    private LocalDateTime localDateTime;


}
