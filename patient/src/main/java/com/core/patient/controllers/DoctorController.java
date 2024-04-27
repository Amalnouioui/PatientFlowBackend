package com.core.patient.controllers;

import com.core.patient.entities.Doctor;
import com.core.patient.entities.Rapport;
import com.core.patient.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @GetMapping("/{doctorKey}")
    public ResponseEntity<Doctor> getRapportbyPatient(@PathVariable("doctorKey") Integer doctorKey) {
        Doctor doctor = doctorService.getDoctor(doctorKey);

        if (doctor!=null) {
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
