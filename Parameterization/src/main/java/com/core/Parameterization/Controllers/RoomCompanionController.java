package com.core.Parameterization.Controllers;

import com.core.Parameterization.Entities.CompanionAndBedLockRequest;
import com.core.Parameterization.Entities.RoomCompanion;
import com.core.Parameterization.Services.RoomCompanionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/roomCompanion")
public class RoomCompanionController {
    @Autowired
    private RoomCompanionService roomCompanionService;







    //////////////



    @PostMapping("/addCompanion")
    public String addCompanion(@RequestBody RoomCompanion roomCompanion) {
        try {
      roomCompanionService.addCompanion(roomCompanion);
            return "accompagnant a été ajouté avec succées !";
        } catch (Exception e) {

            throw new RuntimeException("Erreur lors de l'ajout de l'accompagnant : " + e.getMessage());
        }
    }



    @GetMapping("/getbyId/{id}")
    public ResponseEntity<RoomCompanion> getCompanionbyId(@PathVariable("id") Integer id) {
        Optional<RoomCompanion> roomCompanionOptional = roomCompanionService.getCompanionById(id);
        return roomCompanionOptional.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<RoomCompanion> getCompanionbyName(@PathVariable("name") String  name) {
        Optional<RoomCompanion> roomCompanionOptional = roomCompanionService.getCompanionbyName(name);
        return roomCompanionOptional.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }








    @PostMapping("/saveBedLockedWithAccompagnant")
    public ResponseEntity<String> saveBedLockedWithAccompagnant(@RequestParam Integer roomKey, @RequestBody CompanionAndBedLockRequest request) {
        try {
            roomCompanionService.reservationcompanion(roomKey, request);
            return ResponseEntity.ok("Accompagnant a été enregistré avec succèes.");
        } catch (Exception e) {
            e.printStackTrace(); // ou une autre méthode de journalisation appropriée
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la réservation : " + e.getMessage());
        }
    }
}
