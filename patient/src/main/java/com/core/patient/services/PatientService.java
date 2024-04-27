package com.core.patient.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.core.patient.entities.Patient;
import com.core.patient.entities.enumeration.Gender;
import org.springframework.stereotype.Service;


public interface PatientService {
	 	List<Patient> getAllPatients();
	    void createPatient(Patient patient);
	    Optional<Patient> getPatientById(int id);
	    void deletePatient(int id);
	    void updatePatient(int id, Patient patient);
	    
	    List<Patient> searchPatientsByName(String patientFirstName, String patientLastName);
	    List<Patient> searchPatientsByDateOfBirth(Date dob);
	    List<Patient> searchPatientsByGender(Gender gender);
	List<Patient>searchPatientByFullConditions(String patientFirstName, String patientLastName, Gender patientGender,Date DOB);
	void savePatientData(List<Patient>patientList);
}
