package com.project.MediFlow.Repository;

import com.project.MediFlow.Model.JobStatus;
import com.project.MediFlow.Model.RawDataEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawDataEventRepository extends JpaRepository<RawDataEvent,Long> {

    List<RawDataEvent> findByJobStatus(JobStatus jobStatus);
}
