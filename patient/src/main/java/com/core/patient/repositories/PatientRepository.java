package com.core.patient.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.core.patient.entities.Patient;
import com.core.patient.entities.enumeration.Gender;



public interface PatientRepository extends JpaRepository<Patient,Integer> {
		boolean existsByPatientIdentityNumber(String identityNumber);
		boolean existsByEmail(String email);
		boolean existsByPhone(String phone);


	 	List<Patient> findByPatientFirstName(String firstName);
	    List<Patient> findByPatientLastName(String lastName);
	    List<Patient> findByPatientFirstNameAndPatientLastName(String patientFirstName, String patientLastName);
	    List<Patient> findByPatientBirthDate(Date dob);
	    List<Patient> findByPatientGender(Gender gender);
}
