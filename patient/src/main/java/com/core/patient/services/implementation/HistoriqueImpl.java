package com.core.patient.services.implementation;

import com.core.patient.entities.Historique;
import com.core.patient.entities.Patient;
import com.core.patient.repositories.HistoriqueRepository;
import com.core.patient.repositories.PatientRepository;
import com.core.patient.services.HistorqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HistoriqueImpl implements HistorqueService {
    @Autowired
   private  HistoriqueRepository historiqueRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Override
   public  void createHistory(Historique historique){
        historiqueRepository.save(historique);

    }
    @Override
    public  Historique getHisory(Integer patientKey){
        Optional<Patient> patientOptional=patientRepository.findById(patientKey);
        if(patientOptional.isPresent()){
            Patient patient=patientOptional.get();
            Historique historique=historiqueRepository.findByPatient(patient);
            return historique;

        }
        throw new IllegalStateException("patient n'existe pas!") ;
    }
}
