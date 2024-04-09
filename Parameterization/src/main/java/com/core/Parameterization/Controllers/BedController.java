package com.core.Parameterization.Controllers;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.BedPhysicalCondition;
import com.core.Parameterization.Entities.Enumeration.BedStatus;
import com.core.Parameterization.Entities.Enumeration.BedType;
import com.core.Parameterization.Services.BedService;
import com.core.Parameterization.Services.EquipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/parameterization/bed")
public class BedController {
    @Autowired
    private BedService bedService;
     @Autowired
     private EquipmentService equipmentService;

     //////FIRST PART: BED /////////



    //ADD Bed with  ADDING NEW EQUIPMENT
    @PostMapping
    public String createBed(@RequestBody Map<String, Object> requestBody) {
        try{
        // Extraire les données de l'unité de soin du JSON
        Bed bed = extractBedFromJson(requestBody);

        // Enregistrer l'unité de soin dans la base de données
        bedService.create(bed);

        // Extraire les IDs des équipements du JSON
        List<Long> aEquipmentList = extractEquipmentIdsFromJson(requestBody);


        // Ajouter les équipements à l'unité de soin
        addEquipmentsAndServicesToBed(bed, aEquipmentList);

        return ("Le lit a été ajouté avec succées! ");
    }
    catch(Exception e ){
            return (e.getMessage());
        }
    }

    private Bed extractBedFromJson(Map<String, Object> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(requestBody, Bed.class);
    }


    private List<Long> extractEquipmentIdsFromJson(Map<String, Object> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> equipmentList = (List<Integer>) requestBody.get("equipmentList");
        return equipmentList.stream().map(Long::valueOf).collect(Collectors.toList());
    }

    private void addEquipmentsAndServicesToBed(Bed iBed, List<Long> iEquipmentList) {
        // Ajouter les équipements à l'unité de soin
        for (Long aEquipmentId : iEquipmentList) {
            Optional<Equipment> aEquipmentOptional = equipmentService.getEquipmentById(aEquipmentId);
            if (aEquipmentOptional.isPresent()) {
                Equipment aEquipment = aEquipmentOptional.get();
                // Créer une association entre l'unité de soin et l'équipement
                BedEquipLink aAssociation = new BedEquipLink();
                aAssociation.setBed(iBed);
                aAssociation.setEquipment(aEquipment);
                // Enregistrer l'association dans la base de données
                bedService.addEquipmentsToBed(aAssociation);
            } else {
                // Gérer le cas où l'équipement n'existe pas en retournant une erreur
                throw new IllegalStateException("L'équipement avec l'ID " + aEquipmentId + " n'existe pas.");
            }
        }
    }



    // RETREIVE ALL BEDS
    @GetMapping
    public ResponseEntity<List<Bed>> getAllBeds() {
        try {
            List<Bed> aBeds = bedService.retrieveBeds();
            return new ResponseEntity<>(aBeds, HttpStatus.OK);
        }catch(Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // RETREIVE  BED BY  ITS ID
    @GetMapping("/{id}")
    public ResponseEntity<Bed> getBedById(@PathVariable("id") Integer iBedId) {
        Optional<Bed> aBed = bedService.getBedByKey(iBedId);
        return aBed.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    // RETREIVE  BED  ITS NUMBER
    @GetMapping("bedNumber/{bedNumber}")

    public ResponseEntity<Bed>getBedByItsNumber(@PathVariable ("bedNumber")Integer iBedNumber){
        Bed aBed=bedService.getBedbyNumber(iBedNumber);
        if (aBed!=null){
            return  new ResponseEntity<>(aBed,HttpStatus.OK);
        }else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //  RETREIVE  BEDS  THEIR TYPE
    @GetMapping("bedType/{bedType}")
    public ResponseEntity<List<Bed>>getBedByItsType(@PathVariable("bedType")BedType iBedType){
        List<Bed> aBed=bedService.getBedByType(iBedType);
        if(aBed!=null){
            return new ResponseEntity<>(aBed,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }



    // RETREIVE  BEDS BY THEIR STATUS
    @GetMapping("bedStatue/{bedStatue}")
    public ResponseEntity<List<Bed>>getBedByItsStatue(@PathVariable("bedStatue") BedStatus iBedStatue) {
        List<Bed> aBed= bedService.getByStatue(iBedStatue);
        if (aBed != null) {
            return new ResponseEntity<>(aBed, HttpStatus.OK);
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }


    }


    //  RETREIVE  BEDS BY THEIR PHISCAL SATE
    @GetMapping("BedState/{physicalstate}")
    public ResponseEntity<List<Bed>>getBedByItsphysical_state(@PathVariable("physicalstate") BedPhysicalCondition iPhysicalState) {
        List<Bed> aBed = bedService.getByphysical_state(iPhysicalState);
        if (aBed != null) {
            return new ResponseEntity<>(aBed, HttpStatus.OK);
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }


    }
    // UPDATE A BED


    @PutMapping("/changeBed/{bedKey}")
    public String updateBed(@PathVariable("bedKey") Integer iBedKey, @RequestBody Bed iUpdatedBed) {
        try {
            bedService.update(iBedKey, iUpdatedBed);
            return ("Le lit a été mise à  jour avec succées !");
        } catch (Exception e) {
            return (e.getMessage());
        }
    }

    //DELETE BED WITH EQUIPMENTS
    @DeleteMapping("/{id}")
    public String deleteBed(@PathVariable("id") Integer iBedKey) {
        try {
            Optional<Bed> aBedOptional = bedService.getBedByKey(iBedKey);
            if (aBedOptional.isPresent()) {
                Bed aBed = aBedOptional.get();

                // Remove associated Equipments
                for (BedEquipLink aAssociation : aBed.getBedEquipLinkSet()) {
                    bedService.removeEquipmentFrombed(aBed.getBedKey(), aAssociation.getEquipment().getEquipementkey());
                }

                // Delete the CareUnit
                bedService.delete(iBedKey);

                return ("Le lit a été supprimé avec succées !");
            } else {
                return ("Aucun lit existe avec cet ID ");
            }
        }catch (Exception e){
            return  (e.getMessage());
        }
    }



    ////////SECOND PART: BED AND EQUIPMENTS/////////



    //ADD EQUIPMENTS TO BED
    @PostMapping("/{bedKey}/addEquipments")
    public String addEquipmentsoBed(@PathVariable("bedKey") Integer iBedKey, @RequestBody List<Long> iEquipmentList) {
        try {
            // Récupérer l'unité de soin par son ID
            Optional<Bed> aBedOptional = bedService.getBedByKey(iBedKey);
            if (aBedOptional.isEmpty()) {
                return ("Aucun lit existe avec cet Id ");
            }

            Bed aBed = aBedOptional.get();

            // Parcourir la liste des IDs d'équipements et les associer à l'unité de soin
            for (Long aEquipmentId : iEquipmentList) {
                // Récupérer l'équipement par son ID
                Optional<Equipment> aEquipmentOptional = equipmentService.getEquipmentById(aEquipmentId);
                if (aEquipmentOptional.isEmpty()) {
                    return ("Aucun Equipement existe avec cet Id");
                }

                Equipment aEquipment = aEquipmentOptional.get();

                // Créer une association entre l'unité de soin et l'équipement
                BedEquipLink aAssociation = new BedEquipLink();
                aAssociation.setBed(aBed);
                aAssociation.setEquipment(aEquipment);

                // Enregistrer l'association
                bedService.addEquipmentsToBed(aAssociation);
            }

            return ("Les equipements ont été ajouté avec succées !");
        }catch(Exception e){
            return (e.getMessage());
        }
    }




    // RETRIVE  EQUIPMENTS ASSOCIATED  WITH a SPECIFIC BED
     @GetMapping("/{bedKey}/getEquipments")
     public Set<BedEquipLink> getEquipments(@PathVariable("bedKey") Integer iBedKey) {
        Optional<Bed> aBedOptional = bedService.getBedByKey(iBedKey);
        if (aBedOptional.isPresent()) {
            return bedService.getEquipmentsBybed(iBedKey);
        }else{
            throw  new IllegalStateException("Aucun lit avec cet ID");
        }

    }



    //UPDATE EQUIPMENT IN BED
   @PutMapping("/{bedKey}/updateEquipment")
   public ResponseEntity<String> updateEquipmentInCareUnitPost(@PathVariable("bedKey") Integer iBedKey, @RequestBody UpdateEquipmentRequest iRequest) {
       try {
           bedService.updateEquipmentInBed(iBedKey, iRequest.getOldEquipmentIds(), iRequest.getNewEquipmentIds());
           return new ResponseEntity<>("Equipment updated in Bed successfully", HttpStatus.OK);
       } catch (IllegalArgumentException e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
       }
   }


   //DELETE EQUIPMENTS FROM BED
   @DeleteMapping("/{bedKey}/deleteEquipment/{equipmentId}")
   public String removeEquipmentFromBed(@PathVariable("bedKey") Integer iBedKey, @PathVariable("equipmentId") Long iEquipmentId) {
       try {
           bedService.removeEquipmentFrombed(iBedKey, iEquipmentId);
           return ("L'equipement a été supprimé avec succées");
       } catch (IllegalArgumentException e) {
           return (e.getMessage());
       }
   }




}
