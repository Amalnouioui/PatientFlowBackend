package com.core.patient.repositories;

import com.core.patient.entities.Patient;
import com.core.patient.entities.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RapportRepository extends JpaRepository<Rapport,Integer> {
    List<Rapport> findByPatient(Patient patient);
}
