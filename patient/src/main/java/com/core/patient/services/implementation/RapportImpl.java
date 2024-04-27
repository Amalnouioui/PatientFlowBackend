package com.core.patient.services.implementation;

import com.core.patient.entities.Patient;
import com.core.patient.entities.Rapport;
import com.core.patient.repositories.PatientRepository;
import com.core.patient.repositories.RapportRepository;
import com.core.patient.services.RapportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RapportImpl implements RapportService {
    @Autowired
    private RapportRepository rapportRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Override
   public  List<Rapport> getRapport(Integer patientKey){
        Optional<Patient>patientOptional=patientRepository.findById(patientKey);
        if(patientOptional.isPresent()){
            Patient patient=patientOptional.get();
            return rapportRepository.findByPatient(patient);
        }

        throw  new IllegalStateException("ce patient n'a aucun rapport");
    }

    @Override
    public void createRapport(Rapport rapport){
        rapportRepository.save(rapport);
    }



    @Override
    public void updatedRapport(Integer rapportKey,Rapport rapport){
        Optional<Rapport>rapportOptional=rapportRepository.findById(rapportKey);
        if(rapportOptional.isPresent()){
            Rapport oldRapport=rapportOptional.get();
            oldRapport.setRapportDate(rapport.getRapportDate());
            oldRapport.setCough(rapport.isCough());
            oldRapport.setFatigue(rapport.isFatigue());
            oldRapport.setFever(rapport.isFever());
            oldRapport.setBloodPressure(rapport.getBloodPressure());
            oldRapport.setCholesterol(rapport.getCholesterol());
            oldRapport.setDisease(rapport.getDisease());
            oldRapport.setPatient(rapport.getPatient());
           oldRapport.setDoctor(rapport.getDoctor());
           rapportRepository.save(oldRapport);
        }
    }

    @Override
    public Rapport getById(Integer rapportKey){
        Optional<Rapport>rapportOptional=rapportRepository.findById(rapportKey);
        if(rapportOptional.isPresent()){
            return rapportOptional.get();

        }
        throw  new IllegalStateException("Aucun rapport existe avec cette Id ");
    }

    @Override
    public void deleteRapport(Integer rapportKey){
        Rapport rapport=getById(rapportKey);
        rapportRepository.delete(rapport);
    }
}
