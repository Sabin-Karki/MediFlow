package com.project.MediFlow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor // for data seeder class
public class FieldAliasMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "entity_type",nullable = false)
    String entityType;

    @Column(name = "alias_field",nullable = false)
    private String aliasField;

    @Column(name = "mappedField")
    private String mappedField;
}
