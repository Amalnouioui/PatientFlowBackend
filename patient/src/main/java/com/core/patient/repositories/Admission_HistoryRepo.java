package com.core.patient.repositories;

import com.core.patient.entities.Admission_History;
import com.core.patient.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Admission_HistoryRepo extends JpaRepository<Admission_History,Integer> {

    List<Admission_History> findByPatient(Patient patient);
}
