package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.FieldAliasMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldAliasMappingRepository extends JpaRepository<FieldAliasMapping,Long> {

  List<FieldAliasMapping> findByEntityType(String entityType);
}
