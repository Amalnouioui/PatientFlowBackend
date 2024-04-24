package com.core.patient.services.implementation;

import com.core.patient.entities.Patient;
import com.core.patient.entities.Rapport;
import com.core.patient.repositories.PatientRepository;
import com.core.patient.repositories.RapportRepository;
import com.core.patient.services.RapportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RapportImpl implements RapportService {
    @Autowired
    private RapportRepository rapportRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Override
    public void crateRapport(Rapport rapport){
        rapportRepository.save(rapport);
    }

    @Override

    public  Optional<Rapport> geRapportyPatientKey(Integer patientKey){
        Optional<Patient> patient=patientRepository.findById(patientKey);
        if(patient.isPresent()){
            return rapportRepository.findByPatient(patient.get());
        }
        throw new IllegalStateException("l'Id de patient n'existe pas !");
    }
}
