package com.core.patient.controllers;


import com.core.patient.entities.Patient;
import com.core.patient.entities.Rapport;
import com.core.patient.services.RapportService;
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
    @PostMapping
    public String crateRapport(@RequestBody Rapport rapport) {
        try {
            rapportService.crateRapport(rapport);
            return("Rapport a été enrigistré succées  ");

        } catch(Exception e){
            return (e.getMessage());
        }
    }


    @GetMapping("/{patientKey}")
    public ResponseEntity<Rapport> getPatientById(@PathVariable("patientKey") int patientKey) {
        Optional<Rapport> rapport = rapportService.geRapportyPatientKey(patientKey);
        return rapport.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
