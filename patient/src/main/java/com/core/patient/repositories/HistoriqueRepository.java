package com.core.patient.repositories;

import com.core.patient.entities.Transfer;
import com.core.patient.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueRepository extends JpaRepository<Transfer,Integer> {
List<Transfer> findByPatient(Patient patient);
}
