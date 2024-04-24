package com.core.patient.controllers;

import com.core.patient.entities.Historique;
import com.core.patient.entities.Patient;
import com.core.patient.services.HistorqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/historique")
public class HistoriqueController {
    @Autowired
   private  HistorqueService historqueService;


    @PostMapping
    public String createHistorique(@RequestBody Historique historique) {
        try {
            historqueService.createHistory(historique);
            return("Historique a été enrigistré succées  ");

        } catch(Exception e){
            return (e.getMessage());
        }
    }
    @GetMapping("/getPatientHistory/{patientkey}")
    public ResponseEntity<Historique> getPatientHistory(@PathVariable("patientkey") Integer patientKey) {
        Historique historique = historqueService.getHisory(patientKey);
        if (historique != null) {
            return ResponseEntity.ok().body(historique);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
