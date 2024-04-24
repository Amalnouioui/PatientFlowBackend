package com.core.patient.services;

import com.core.patient.entities.Rapport;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RapportService {
    void crateRapport(Rapport rapport);
    Optional<Rapport> geRapportyPatientKey(Integer patientKey);
}
