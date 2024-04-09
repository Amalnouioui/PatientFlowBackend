package com.core.patient.services.implementation;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.patient.entities.Patient;
import com.core.patient.entities.enumeration.Gender;
import com.core.patient.repositories.PatientRepository;
import com.core.patient.services.PatientService;

@Service
public class PatientServiceImpl implements PatientService {
	
	@Autowired
    private PatientRepository patientRepository;

	  @Override
	    public List<Patient> getAllPatients() {
	        return patientRepository.findAll();
	    }

    @Override
    public void createPatient(Patient patient) {
    	 if (patientRepository.existsByPatientIdentityNumber(patient.getPatientIdentityNumber())) {
            
             throw new IllegalArgumentException("identité existe !");
    	 }else if(patientRepository.existsByEmail(patient.getEmail())) {
            	 
             throw new IllegalArgumentException("email existe !");
    	 }else if(patientRepository.existsByPhone(patient.getPhone())) {
             throw new IllegalArgumentException("numéro de telephone existe !");

    	 }else  {
        	 patientRepository.save(patient);}
         
    }

    @Override
    public Optional<Patient> getPatientById(int id) {
        return patientRepository.findById(id);
    }

    @Override
    public void deletePatient(int id) {
        patientRepository.deleteById(id);
    }

    @Override
    public void updatePatient(int id, Patient updatedPatient) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            
            // Check if the new identity number conflicts with an existing one
            if (!patient.getPatientIdentityNumber().equals(updatedPatient.getPatientIdentityNumber())
                    && patientRepository.existsByPatientIdentityNumber(updatedPatient.getPatientIdentityNumber())) {
                throw new IllegalArgumentException("Another patient with the same identity number already exists");
            }
            
            // Update other fields
            patient.setPatientFirstName(updatedPatient.getPatientFirstName());
            patient.setPatientLastName(updatedPatient.getPatientLastName());
            patient.setPatientBirthDate(updatedPatient.getPatientBirthDate());
            patient.setPatientGender(updatedPatient.getPatientGender());
            patient.setPatientIdentityNumber(updatedPatient.getPatientIdentityNumber());
            patient.setIdentity(updatedPatient.getIdentity());
            patient.setPatientNationality(updatedPatient.getPatientNationality());
            patient.setAdress(updatedPatient.getAdress());
            patient.setEmail(updatedPatient.getEmail());
            patient.setCountry(updatedPatient.getCountry());
            patient.setSocialStatus(updatedPatient.getSocialStatus());
            patient.setPhone(updatedPatient.getPhone());
            patient.setPatientSize(updatedPatient.getPatientSize());
            patient.setPatientWeight(updatedPatient.getPatientWeight());
            patient.setPatientRemarks(updatedPatient.getPatientRemarks());
            
            
            // Save and return the updated patient
           patientRepository.save(patient);
        } else {
            // Handle not found scenario
        	throw new IllegalArgumentException("Ce patient n'existe pas");
           
        }
    }
    
    
    @Override
    public List<Patient> searchPatientsByName(String patientFirstName, String patientLastName) {
        if (patientFirstName != null && patientLastName != null) {
            return patientRepository.findByPatientFirstNameAndPatientLastName(patientFirstName, patientLastName);
        } else if (patientFirstName != null) {
            return patientRepository.findByPatientFirstName(patientFirstName);
        } else if (patientLastName != null) {
            return patientRepository.findByPatientLastName(patientLastName);
        } else {
            return getAllPatients(); // Retourner tous les patients si aucun critère de recherche n'est spécifié
        }
    }

    @Override
    public List<Patient> searchPatientsByDateOfBirth(Date dob) {
        return patientRepository.findByPatientBirthDate(dob);
    }

    @Override
    public List<Patient> searchPatientsByGender(Gender gender) {
        return patientRepository.findByPatientGender(gender);
    }
	
	

}
