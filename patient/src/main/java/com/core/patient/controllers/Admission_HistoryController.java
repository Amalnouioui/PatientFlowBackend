package com.core.patient.controllers;

import com.core.patient.entities.Admission_History;
import com.core.patient.services.Admission_HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admissionhistorique")
public class Admission_HistoryController {
@Autowired
private Admission_HistoryService admissionHistoryService;
    @PostMapping
    public String createHistorique(@RequestBody Admission_History historique) {
        try {
            admissionHistoryService.createHistory(historique);
            return("Historique a été enrigistré succées  ");

        } catch(Exception e){
            return (e.getMessage());
        }
    }
    @GetMapping("/getPatientHistory/{patientkey}")
    public ResponseEntity<List<Admission_History>> getPatientHistory(@PathVariable("patientkey") Integer patientKey) {
        List<Admission_History> historique = admissionHistoryService.getHisory(patientKey);
        if (historique != null) {
            return ResponseEntity.ok().body(historique);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
