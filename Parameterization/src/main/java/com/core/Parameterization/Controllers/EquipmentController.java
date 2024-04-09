package com.core.Parameterization.Controllers;

import com.core.Parameterization.Entities.Equipment;
import com.core.Parameterization.Services.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/parameterization/equipment")
public class EquipmentController {

        @Autowired
        private EquipmentService equipmentService;

// Get all equipments
        @GetMapping
        public ResponseEntity<List<Equipment>> getAllEquipments() {
            List<Equipment> equipments = equipmentService.getAllEquipments();
            return new ResponseEntity<>(equipments, HttpStatus.OK);
        }


    //get equipment in careunit
    @GetMapping("/equipmentCareunit")
    public ResponseEntity<List<Equipment>>getCareunitEquipments(){
            List<Equipment>aEquipments=equipmentService.getCareunitEquipment();
        return new ResponseEntity<>(aEquipments,HttpStatus.OK);}


    //GetEquipment in bed
    @GetMapping("/equipmentBed")
    public ResponseEntity<List<Equipment>>getBedsEquipments(){
        List<Equipment>aEquipments=equipmentService.getBedEquipment();
        return new ResponseEntity<>(aEquipments,HttpStatus.OK);}




        // Get equipment by ID
        @GetMapping("/{id}")
        public ResponseEntity<Equipment> getEquipmentById(@PathVariable("id") Long iEquipmentId) {
            Optional<Equipment> aEquipment = equipmentService.getEquipmentById(iEquipmentId);
            return aEquipment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        // Add a new equipment
        @PostMapping
        public String  addEquipment(@RequestBody Equipment iEquipment) {
            try {
                Equipment newEquipment = equipmentService.addEquipment(iEquipment);
                return ("L'equipement a été ajouter avec succées ");
            } catch (Exception e) {
                return (e.getMessage());
            }
        }

        // Update an existing equipment
        @PutMapping("/updateEquipment/{id}")
        public String  updateEquipment(@PathVariable("id") Long iEquipmentId, @RequestBody Equipment iEquipment) {
            Optional<Equipment> aExistingEquipment = equipmentService.getEquipmentById(iEquipmentId);
            if (aExistingEquipment.isPresent()) {
                iEquipment.setEquipementkey(iEquipmentId);
                Equipment aUpdatedEquipment = equipmentService.updateEquipment(iEquipment);
                return ("L'equipment a été mise a jour avec succées ");
            } else {
                return ("L'equipement n'existe pas !");
            }
        }

        // Delete an equipment
        @DeleteMapping("/{id}")
        public String  deleteEquipment(@PathVariable("id") Long iEquipmentId) {
            Optional<Equipment> aExistingEquipment = equipmentService.getEquipmentById(iEquipmentId);
            if (aExistingEquipment.isPresent()) {
                equipmentService.deleteEquipment(iEquipmentId);
                return ("L'equipement a été supprimer avec succées" );
            } else {
                return ("Echec de supprimer L'equipement");
            }
        }
    }



