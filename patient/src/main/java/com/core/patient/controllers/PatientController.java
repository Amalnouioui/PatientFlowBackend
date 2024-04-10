package com.core.patient.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core.patient.entities.Patient;
import com.core.patient.entities.enumeration.Gender;
import com.core.patient.services.PatientService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/patients")
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	
	 @GetMapping
	    public ResponseEntity<List<Patient>> getAllPatients() {
	        List<Patient> patients = patientService.getAllPatients();
	        return new ResponseEntity<>(patients, HttpStatus.OK);
	    }
	 
	 @GetMapping("/{id}")
	    public ResponseEntity<Patient> getPatientById(@PathVariable("id") int id) {
	        Optional<Patient> patient = patientService.getPatientById(id);
	        return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
	                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	    }
	 
	 @PostMapping
	    public String createPatient(@RequestBody Patient patient) {
	        try {
	            patientService.createPatient(patient);
	            return("Patient enrigistré ");
	            
	        } catch(Exception e){
	            return (e.getMessage());
	        }
	    }
	 
	 @PutMapping("/{id}")
	    public String updatePatient(@PathVariable("id") int id, @RequestBody Patient updatedPatient) {
	        try {
	            patientService.updatePatient(id, updatedPatient);
	            return("les information de ce patient sont modifié ");
	        } catch (Exception e) {
	            return (e.getMessage());
	        }
	    }
	 
	 @DeleteMapping("/{id}")
	    public String deletePatient(@PathVariable("id") int id) {
		 
		 Optional<Patient> RoomOp=patientService.getPatientById(id);
		 	if(RoomOp.isPresent()) {
		 		patientService.deletePatient(id);
		        return ("Patient est supprimé ");
		 	}else {
		 		return("Aucun patient existe avec ce id");
		 	}
	        
	    }
	 
	 @GetMapping("/search/by-name")
	    public ResponseEntity<List<Patient>> searchPatientsByName(
	            @RequestParam(name = "patientFirstName", required = true) String patientFirstName,
	            @RequestParam(name = "patientLastName", required = false) String patientLastName) {
	        List<Patient> patients = patientService.searchPatientsByName(patientFirstName, patientLastName);
	        return new ResponseEntity<>(patients, HttpStatus.OK);
	    }

	    @GetMapping("/search/by-dob")
	    public ResponseEntity<List<Patient>> searchPatientsByDateOfBirth(
	            @RequestParam("dob") Date dob) {
	        List<Patient> patients = patientService.searchPatientsByDateOfBirth(dob);
	        return new ResponseEntity<>(patients, HttpStatus.OK);
	    }

	    @GetMapping("/search/by-gender")
	    public ResponseEntity<List<Patient>> searchPatientsByGender(
	            @RequestParam("gender") Gender gender) {
	        List<Patient> patients = patientService.searchPatientsByGender(gender);
	        return new ResponseEntity<>(patients, HttpStatus.OK);
	    }



	@GetMapping("/search/fullNameDOBSexe")
	public ResponseEntity<List<Patient>> searchPatientbyFullNameDOBAndSexe(
			@RequestParam(name = "patientFirstName", required = true) String patientFirstName,
			@RequestParam(name = "patientLastName", required = true) String patientLastName,
			@RequestParam(name = "patientGender", required = true) Gender patientGender,
			@RequestParam(name = "patientBirthDate", required = true)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date patientBirthDate){
		List<Patient> patients = patientService.searchPatientByFullConditions(patientFirstName, patientLastName,patientGender,patientBirthDate);
		return new ResponseEntity<>(patients, HttpStatus.OK);
	}

}
