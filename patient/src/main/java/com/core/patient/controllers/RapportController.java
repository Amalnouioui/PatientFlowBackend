package com.core.patient.controllers;

import com.core.patient.entities.Patient;
import com.core.patient.entities.Rapport;
import com.core.patient.services.PatientService;
import com.core.patient.services.RapportService;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/rapport")
public class RapportController {

    @Autowired
    private RapportService rapportService;



    @GetMapping("/{patientKey}")
    public ResponseEntity<List<Rapport>> getRapportbyPatient(@PathVariable("patientKey") Integer patientKey) {
        List<Rapport> rapports = rapportService.getRapport(patientKey);

        if (!rapports.isEmpty()) {
            return new ResponseEntity<>(rapports, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public String createPatient(@RequestBody Rapport rapport) {
        try {
            rapportService.createRapport(rapport);
            return("rapport a été enrigistré succées  ");

        } catch(Exception e){
            return (e.getMessage());
        }
    }

    @PutMapping("/{patientKey}")
    public String updatRapport(@PathVariable("patientKey") Integer rapportKey, @RequestBody Rapport updatedRapport) {
        try {
            rapportService.updatedRapport(rapportKey, updatedRapport);
            return("les informations de ce rapport sont modifiés ");
        } catch (Exception e) {
            return (e.getMessage());
        }
    }

    @DeleteMapping("/{rapportKey}")
    public String deleteRapport(@PathVariable("rapportKey") Integer rapportKey) {

            rapportService.deleteRapport(rapportKey);
            return ("Rapport  est supprimé ");
        }

    }


