package com.core.Parameterization.Controllers;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.BedStatus;
import com.core.Parameterization.Entities.Enumeration.RoomStatus;
import com.core.Parameterization.Entities.Enumeration.UnitStatus;
import com.core.Parameterization.Entities.Enumeration.RoomType;
import com.core.Parameterization.Services.BedService;
import com.core.Parameterization.Services.CareUnitService;
import com.core.Parameterization.Services.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/parameterization/room")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private BedService bedService;
    @Autowired
    private CareUnitService careUnitService;


    // CREATE A ROOM
    @PostMapping

    public String createRoom(@RequestBody Room iRoom) {
        try {
            roomService.create(iRoom);
            return (" La chambre a été ajouté avec succées ! ");

        }catch(Exception e){
            return (e.getMessage());
        }
    }


    // RETREIVE ALL ROOM
    @GetMapping
    public ResponseEntity<List<Room>> GetAllRooms(){
        try {
            List<Room> rooms = roomService.retreiveRooms();
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        }catch(Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // RETREIVE ROOM BY ITS ID
    @GetMapping("/{id}")
    public ResponseEntity<Room> GetRoomById (@PathVariable("id") Integer iRoomKey){
        Optional<Room>room=roomService.getRoomByKey(iRoomKey);
        return room.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }



    // RETREIVE ROOM BY ITS NAME
    @GetMapping("/roomName/{roomName}")
    public ResponseEntity<Room>getRoomByname(@PathVariable("roomName")Integer iRoomName){
        Room aRoom=roomService.getRoombyItsName(iRoomName);
        if(aRoom!=null){
            return  new ResponseEntity<>(aRoom,HttpStatus.OK);

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    //  RETREIVE ROOM BY THEIR TYPE
    @GetMapping("/roomType/{roomType}")
    public ResponseEntity<List<Room>> findRoomWithType(@PathVariable("roomType") RoomType iRoomType){
        List<Room>aRoom=roomService.getRoomByType(iRoomType);
        if(aRoom!=null){
            return new ResponseEntity<>(aRoom,HttpStatus.OK);

        }else{
            return new ResponseEntity<>(aRoom,HttpStatus.NOT_FOUND);
        }
    }



    // RETREIVE ROOM BY THEIR STATUS
    @GetMapping("/roomStatue/{roomStatue}")
    public ResponseEntity<List<Room>> findRoomWithStatue(@PathVariable("roomStatue") RoomStatus iRoomStatue){
        List<Room>aRooms=roomService.getRoomByStatue(iRoomStatue);
        if(aRooms!=null){
            return new ResponseEntity<>(aRooms,HttpStatus.OK);

        }else{
            return new ResponseEntity<>(aRooms,HttpStatus.NOT_FOUND);
        }
    }




    // UPDATE A ROOM
    @PutMapping("/changeroom/{roomKey}")
    public String updateRoom( @PathVariable("roomKey") Integer iRoomKey,@RequestBody Room iNewRoom) {

        try {
            roomService.update(iRoomKey,iNewRoom);
            return ("La chambre a été mise à  jour avec succées ! ");
        } catch (Exception e) {
            return(e.getMessage());
        }
    }



    // DELETE A ROOM BY ITS ID
    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable("id") Integer iRoomKey) {
        Optional<Room> aExistingRoom = roomService.getRoomByKey(iRoomKey);
        if (aExistingRoom.isPresent()) {
            roomService.delete(iRoomKey);
            return ("La chambre a étét supprimé avc succée ");
        } else {
            return ("Aucune chambre existe avec cette Id");
        }
    }



    // ADD A BED IN A ROOM
    @PostMapping("/{id}/addBed")
    public String  addBedRoom(@PathVariable("id") Integer iRoomKey, @RequestBody Bed iBed) {
      try {
          Optional<Room> aRoomOptional = roomService.getRoomByKey(iRoomKey);
          if (aRoomOptional.isPresent()) {
              Room aRoomCpacity=aRoomOptional.get();
              roomService.addBed(aRoomOptional.get(), iBed);
              return ("Le lit a été ajouté avec succées ");
          }


          else {
              return ("Aucune chambre existe avec cet ID");
          }
      }catch(Exception e){
          return(e.getMessage());
      }
    }




    // RETREIVE ALL BEDS IN A SPECIFIC ROOM
    @GetMapping("/{id}/getBed")
    public ResponseEntity<List<Bed>>getBed(@PathVariable("id")Integer iRoomkey){
        Optional<Room> aRoom = roomService.getRoomByKey(iRoomkey);
        if (aRoom.isPresent()) {
            List<Bed> aBedList = roomService.getBed(aRoom.get());
            return new ResponseEntity<>(aBedList,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    //GET BED BY NAME IN A ROOM
    @GetMapping("/{roomKey}/bedNumber/{bedNumber}")
    public ResponseEntity<Bed> getRoomByName(@PathVariable("roomKey") Integer iRoomKey, @PathVariable("bedNumber") Integer iBedNumber) {
        Optional<Room> aRoomOptional = roomService.getRoomByKey(iRoomKey);
        if (aRoomOptional.isPresent()) {
            Room aRoom = aRoomOptional.get();
            List<Bed> aBeds = aRoom.getRoomBed();

            // Check if the room exists in the care unit
            Optional<Bed> aBedOptional = aBeds.stream().filter(bed -> bed.getBedNumber().equals(iBedNumber)).findFirst();
            if (aBedOptional.isPresent()) {
                Bed aBed = aBedOptional.get();
                return new ResponseEntity<>(aBed, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Get BED BY STATUE IN A ROOM
    @GetMapping("/{roomKey}/bedStatus/{bedStatus}")
    public ResponseEntity<List<Bed>> getBedByStatus(
            @PathVariable("roomKey") Integer iRoom,
            @PathVariable("bedStatus") BedStatus iBedStatus) {

        Optional<Room> aRoomOptional = roomService.getRoomByKey(iRoom);
        if (aRoomOptional.isPresent()) {
            Room aRoom = aRoomOptional.get();
            List<Bed> aBeds = aRoom.getRoomBed();

            // Créer une liste pour stocker les chambres qui correspondent au statut spécifié
            List<Bed> bedWithStatus = new ArrayList<>();

            // Parcourir toutes les chambres de l'unité de soins
            for (Bed aBed : aBeds) {
                // Vérifier si le statut de la chambre correspond au statut spécifié
                if (aBed.getBedStatue() == iBedStatus) {
                    bedWithStatus.add(aBed);
                }
            }

            if (!bedWithStatus.isEmpty()) {
                return new ResponseEntity<>(bedWithStatus, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    //UPDATE A BED INSIDE A SPECIFIC ROOM
    @PutMapping("/{roomKey}/updateBed/{bedKey}")
    public String updateBedInRoom(
            @PathVariable("roomKey") Integer iRoomKey,
            @PathVariable("bedKey") Integer iBedKey,
            @RequestBody Bed iNewBed) {

        // Récupérer la chambre depuis la base de données par sa clé unique
        Optional<Room> aRoomOptional = roomService.getRoomByKey(iRoomKey);
        if (aRoomOptional.isPresent()) {

            // Récupérer le lit depuis la base de données par sa clé unique
            Optional<Bed> aBedOptional = bedService.getBedByKey(iBedKey);
            if (aBedOptional.isPresent()) {

                Bed aExistingBed = aBedOptional.get();

                // Vérifier si le lit récupéré appartient à la chambre spécifiée
                if (!aExistingBed.getRoomBed().getRoomKey().equals(iRoomKey)) {
                    return ("Le lit n'appartient pas a cette chambre !");
                }

                // Mettre à jour les données du lit avec les nouvelles données
                aExistingBed.setBedNumber(iNewBed.getBedNumber());
                aExistingBed.setBedType(iNewBed.getBedType());
                aExistingBed.setBedDescription(iNewBed.getBedDescription());
                aExistingBed.setBedStatue(iNewBed.getBedStatue());
                aExistingBed.setBedPurchaseDate(iNewBed.getBedPurchaseDate());
                aExistingBed.setBedCleaningStatus(iNewBed.getBedCleaningStatus());
                aExistingBed.setExpirationDate(iNewBed.getExpirationDate());
                aExistingBed.setPhysicalState(iNewBed.getPhysicalState());
                aExistingBed.setRemainingLifeSpan(iNewBed.getRemainingLifeSpan());
                aExistingBed.setPoids(iNewBed.getPoids());

                // Ne pas mettre à jour la chambre à laquelle le lit appartient, car cela peut causer des problèmes de cohérence de données

                try {
                    // Mettre à jour le lit dans la base de données
                    bedService.updateBed(aExistingBed);
                    return ("Le lit a été mise à  jour avec succées   ");
                } catch (Exception e) {
                    // Gérer l'erreur de manière appropriée (par exemple, enregistrer l'erreur, retourner une réponse d'erreur)
                    return (e.getMessage());
                }
            } else {
                // Salle avec le roomKey donné non trouvée
                return ("Aucun lit existe avec cet ID !");
            }
        } else {
            return ("Aucune chambre existe avec cet ID !");
        }
    }


    //DELETE A BED FROM A SPECIFIC ROOM
    @DeleteMapping("/{roomKey}/deleteBed/{bedKey}")
    public String deleteBed(@PathVariable("roomKey") Integer iRoomKey, @PathVariable("bedKey") Integer iBedKey) {
        try {
            Optional<Room> aRoomOptional = roomService.getRoomByKey(iRoomKey);

            if (aRoomOptional.isPresent()) {
                Room aRoom = aRoomOptional.get();

                Optional<Bed> aBedOptional = aRoom.getRoomBed().stream()
                        .filter(bed -> bed.getBedKey().equals(iBedKey))
                        .findFirst();


                if (aBedOptional.isPresent()) {
                    Bed aBed = aBedOptional.get();
                    // Suppression de lit du chambre
                    aRoom.getRoomBed().remove(aBed);
                    // Suppression de le lit de la base de données
                    bedService.deleteBed(aBed);

                    // Mettez à jour la chambre  dans la base de données
                    roomService.updateRoom(aRoom);

                    return ("Le lit a été supprimé avec succées !");
                } else {
                    // le lit avec la clé donnée n'a pas été trouvé dans la chambre
                    return ("Aucun lit existe avec cet Id");
                }
            } else {
                // La chambre avec l'ID donné n'a pas été trouvée
                return ("Aucune chambre avec cet ID ");
            }
        } catch (Exception e) {
        return ("Failed to delete bed in room ");
        }
    }





}
