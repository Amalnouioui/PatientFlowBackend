package com.core.patient.services.implementation;

import com.core.patient.entities.Admission_History;
import com.core.patient.entities.Patient;
import com.core.patient.repositories.Admission_HistoryRepo;
import com.core.patient.repositories.PatientRepository;
import com.core.patient.services.Admission_HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Admission_HistoryServiceImpl implements Admission_HistoryService {


@Autowired
private Admission_HistoryRepo admissionHistoryRepo;
@Autowired
private PatientRepository patientRepository;
    @Override
    public  void createHistory(Admission_History historique){
        admissionHistoryRepo.save(historique);

    }
    @Override
    public List<Admission_History> getHisory(Integer patientKey){
        Optional<Patient> patientOptional=patientRepository.findById(patientKey);
        if(patientOptional.isPresent()){

            List<Admission_History> historique = admissionHistoryRepo.findByPatient(patientOptional.get());
            return historique;

        }
        throw new IllegalStateException("patient n'existe pas!") ;
    }
}

