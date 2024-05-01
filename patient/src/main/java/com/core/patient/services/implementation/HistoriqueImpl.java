package com.core.patient.services.implementation;

import com.core.patient.entities.Transfer;
import com.core.patient.entities.Patient;
import com.core.patient.repositories.HistoriqueRepository;
import com.core.patient.repositories.PatientRepository;
import com.core.patient.services.HistorqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueImpl implements HistorqueService {
    @Autowired
   private  HistoriqueRepository historiqueRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Override
   public  void createHistory(Transfer transfer){
        historiqueRepository.save(transfer);

    }
    @Override
    public  List<Transfer> getHisory(Integer patientKey){
        Optional<Patient> patientOptional=patientRepository.findById(patientKey);
        if(patientOptional.isPresent()){

             List<Transfer> transfer = historiqueRepository.findByPatient(patientOptional.get());
             return transfer;

        }
        throw new IllegalStateException("patient n'existe pas!") ;
    }
}
