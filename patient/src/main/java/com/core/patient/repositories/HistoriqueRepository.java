package com.core.patient.repositories;

import com.core.patient.entities.Historique;
import com.core.patient.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueRepository extends JpaRepository<Historique,Integer> {
List<Historique> findByPatient(Patient patient);
}
