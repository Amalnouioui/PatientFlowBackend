package com.core.patient.controllers;

import com.core.patient.entities.Transfer;
import com.core.patient.services.HistorqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/historique")
public class DeplacementHistoryController {
    @Autowired
   private  HistorqueService historqueService;


    @PostMapping
    public String createHistorique(@RequestBody Transfer transfer) {
        try {
            historqueService.createHistory(transfer);
            return("Historique a été enrigistré succées  ");

        } catch(Exception e){
            return (e.getMessage());
        }
    }
    @GetMapping("/getPatientHistory/{patientkey}")
    public ResponseEntity<List<Transfer>> getPatientHistory(@PathVariable("patientkey") Integer patientKey) {
        List<Transfer> transfer = historqueService.getHisory(patientKey);
        if (transfer != null) {
            return ResponseEntity.ok().body(transfer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
