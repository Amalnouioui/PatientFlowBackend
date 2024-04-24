package com.core.Parameterization.Controllers;


import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.*;
import com.core.Parameterization.Services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/parameterization/careunit")
public class CareUnitController {
    @Autowired
    private CareUnitService careUnitService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private EquipmentService equipmentService;
@Autowired
private  BedLockedService bedLockedService;

    //ADD EQUIPMENT AND SERVICE TO CAREUNIT WHEN ADDING NEW CAREUNIT
    @PostMapping

    public String createCareUnit(@RequestBody Map<String, Object> iRequestBody) {
        try {
            // Extraire les données de l'unité de soin du JSON
            CareUnit aCareUnit = extractCareUnitFromJson(iRequestBody);

            // Enregistrer l'unité de soin dans la base de données
            CareUnit aCareUnitService = careUnitService.create(aCareUnit);

            // Extraire les IDs des équipements du JSON
            List<Long> aEquipmentList = extractEquipmentIdsFromJson(iRequestBody);


            // Ajouter les équipements à l'unité de soin
            addEquipmentToCareUnit(aCareUnit, aEquipmentList);

            return ("l'unité de soin a été ajouté avec succées");
        } catch (Exception e) {
            return (e.getMessage());
        }
    }


    //EXTRACT CAREUNIT FROM THE BODY
    private CareUnit extractCareUnitFromJson(Map<String, Object> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(requestBody, CareUnit.class);
    }

    //EXTRACT THE EQUIPMENT LIST IDS FROM THE BODY
    private List<Long> extractEquipmentIdsFromJson(Map<String, Object> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> aEquipmentList = (List<Integer>) requestBody.get("equipmentList");
        if (aEquipmentList != null) {
            return aEquipmentList.stream().map(Long::valueOf).collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // Retourner une liste vide si serviceIds est null
        }

    }


    //ADD CAREUNIT , EQUIPMENT AND SERVICE
    private void addEquipmentToCareUnit(CareUnit iCareUnit, List<Long> iEquipmentList) {
        // Ajouter les équipements à l'unité de soin
        for (Long aEquipmentId : iEquipmentList) {
            Optional<Equipment> aEquipmentOptional = equipmentService.getEquipmentById(aEquipmentId);
            if (aEquipmentOptional.isPresent()) {
                Equipment aEquipment = aEquipmentOptional.get();
                // Créer une association entre l'unité de soin et l'équipement
                CareUnitEquipLink aAssociation = new CareUnitEquipLink();
                aAssociation.setCareUnit(iCareUnit);
                aAssociation.setEquipment(aEquipment);
                // Enregistrer l'association dans la base de données
                careUnitService.addEquipmentToCareUnit(aAssociation);
            } else {
                // Gérer le cas où l'équipement n'existe pas en retournant une erreur
                throw new IllegalStateException("L'équipement avec l'ID " + aEquipmentId + " n'existe pas.");
            }
        }


    }


    // RETRIVE ALL CAREUNITS
    @GetMapping

    public ResponseEntity<List<CareUnit>> getAllCareUnit() {
        try {
            List<CareUnit> aAllCareUnits = careUnitService.retreivesAllCareUnits();

            return new ResponseEntity<>(aAllCareUnits, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    //RETREIVE CAREUNIT BY ITS ID
    @GetMapping("/{id}")
    public ResponseEntity<CareUnit> getCareUnitById(@PathVariable("id") Integer iCareunitKey) {
        Optional<CareUnit> aCareUnit = careUnitService.getCareUnitByKey(iCareunitKey);
        return aCareUnit.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }

    // RETREIVE CAREUNIT BY ITS NAME

    @GetMapping("/careunitName/{careunitName}")
    public ResponseEntity<CareUnit> getCareUnitByName(@PathVariable("careunitName") String iCareunitName) {
        CareUnit aCareUnit = careUnitService.getCareUnitbyNme(iCareunitName);
        if (aCareUnit != null) {
            return new ResponseEntity<>(aCareUnit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // RETREIVE CAREUNITS BY THEIR STATUS
    @GetMapping("/careunitStatue/{careunitStatue}")
    public ResponseEntity<List<CareUnit>> getCareUnitStatue(@PathVariable("careunitStatue") UnitStatus iCareunitStatue) {
        List<CareUnit> aCareUnit = careUnitService.getCareUnitByStatue(iCareunitStatue);
        if (aCareUnit != null) {
            return new ResponseEntity<>(aCareUnit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }


    // UPDATE CAREUNIT
    @PutMapping("/changeCareUnit/{careunitKey}")
    public String ChangeCareUnit(@PathVariable("careunitKey") Integer iCareunitKey, @RequestBody CareUnit iNewCareUnit) {
        try {
            careUnitService.update(iCareunitKey, iNewCareUnit);
            return ("l'unite de soin a été mis a jour avec succées ");
        } catch (Exception e) {
            return (e.getMessage());
        }
    }


    //DELETE CAREUNIT WITH THE EQUIPMENT AND SERVICE ASSOCIATION
    @DeleteMapping("/{id}")
    public String deleteCareUnitAndAssociatedEntities(@PathVariable("id") Integer iId) {
        try {
            Optional<CareUnit> aExistingCareUnitOptional = careUnitService.getCareUnitByKey(iId);
            CareUnit aExistingCareUnit = aExistingCareUnitOptional.get();

            // Remove associated Equipments
            for (CareUnitEquipLink aAssociation : aExistingCareUnit.getCareUnitEquipLinkSet()) {
                careUnitService.removeEquipmentFromCareUnit(aExistingCareUnit.getCareunitKey(), aAssociation.getEquipment().getEquipementkey());
            }


            // Delete the CareUnit
            careUnitService.delete(iId);

            return ("L'unite de soin a été supprimé avec succées ");
        } catch (Exception e) {
            return (e.getMessage());
        }
    }


    //Search
    @GetMapping("/search")
    public List<CareUnit> searchByCriteria(@RequestParam int careunitCapacity, @RequestParam UnitType careuniType, @RequestParam UnitStatus careunitStatue) {
        // Appelez la méthode du service pour effectuer la recherche
        return careUnitService.searchByCriteria(careunitCapacity, careuniType, careunitStatue);
    }

    //////       SECOND PART: CAREUNIT AND ROOM   /////////


    //ADD  ROOM IN CAREUNI
    @PostMapping("/{id}/AddRoom")
    public String addRoomToCareUnit(@PathVariable("id") Integer iCareunitKey, @RequestBody Room iRoom) {
        try {
            Optional<CareUnit> aCareUnit = careUnitService.getCareUnitByKey(iCareunitKey);

            if (aCareUnit.isPresent()) {

                CareUnit aCareuniCapacity = aCareUnit.get();
                careUnitService.addRoom(aCareUnit.get(), iRoom);
                return ("la chambre a été ajouté  avec succées ");
            } else {
                return ("Aucune unité de soin existe avec cet ID !");
            }
        } catch (Exception e) {
            return (e.getMessage());
        }
    }


    // RETREIVE ALL ROOMS ASSOCIATED WITH CAREUNIT
    @GetMapping("/{id}/GetRoom")
    public ResponseEntity<List<Room>> GetRoomListByCareUnit(@PathVariable("id") Integer iCareunitKey) {
        Optional<CareUnit> aCareUnit = careUnitService.getCareUnitByKey(iCareunitKey);
        if (aCareUnit.isPresent()) {
            List<Room> aRooms = careUnitService.getRoom(aCareUnit.get());
            return new ResponseEntity<>(aRooms, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    //RETREIVE ROOM by ITS NAME IN A SPECIFIC CAREUNIT
    @GetMapping("/{careunitKey}/roomName/{roomName}")
    public ResponseEntity<Room> getRoomByName(@PathVariable("careunitKey") Integer iCareUnitKey, @PathVariable("roomName") Integer iRoomName) {
        Optional<CareUnit> aCareUnitOptional = careUnitService.getCareUnitByKey(iCareUnitKey);
        if (aCareUnitOptional.isPresent()) {
            CareUnit aCareUnit = aCareUnitOptional.get();
            List<Room> aRooms = aCareUnit.getRooms();

            // Check if the room exists in the care unit
            Optional<Room> aRoomOptional = aRooms.stream().filter(room -> room.getRoomName().equals(iRoomName)).findFirst();
            if (aRoomOptional.isPresent()) {
                Room aRoom = aRoomOptional.get();
                return new ResponseEntity<>(aRoom, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /////RETREIVE  ROOM BY STATUS IN A SPECIFIC  CAREUNIT
    @GetMapping("/{careunitKey}/roomStatue/{roomStatue}")
    public ResponseEntity<List<Room>> getRoomByStatus(
            @PathVariable("careunitKey") Integer iCareUnitKey,
            @PathVariable("roomStatue") RoomStatus iRoomStatue) {


        Optional<CareUnit> aCareUnitOptional = careUnitService.getCareUnitByKey(iCareUnitKey);
        if (aCareUnitOptional.isPresent()) {
            CareUnit aCareUnit = aCareUnitOptional.get();
            List<Room> aRooms = aCareUnit.getRooms();

            // Créer une liste pour stocker les chambres qui correspondent au statut spécifié
            List<Room> aRoomsWithStatus = new ArrayList<>();

            // Parcourir toutes les chambres de l'unité de soins
            for (Room aRoom : aRooms) {
                // Vérifier si le statut de la chambre correspond au statut spécifié
                if (aRoom.getRoomStatue() == iRoomStatue) {
                    aRoomsWithStatus.add(aRoom);
                }
            }

            if (!aRoomsWithStatus.isEmpty()) {
                return new ResponseEntity<>(aRoomsWithStatus, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // Mise à jour de la méthode pour retourner un message personnalisé
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{careunitKey}/UpdateRoom/{roomKey}")
    public String updateRoomInCareUnit(
            @PathVariable("careunitKey") Integer iCareunitKey,
            @PathVariable("roomKey") Integer iRoomKey,
            @RequestBody Room iNewRoom) {

        Optional<CareUnit> aCareUnitOptional = careUnitService.getCareUnitByKey(iCareunitKey);
        if (aCareUnitOptional.isPresent()) {
            Optional<Room> aRoomOptional = roomService.getRoomByKey(iRoomKey);

            if (aRoomOptional.isPresent()) {
                Room aExistingRoom = aRoomOptional.get();

                if (!aExistingRoom.getCareunitRoom().getCareunitKey().equals(iCareunitKey)) {
                    return ("La chambre n'appartient pas a cette unité de soin  !");
                }

                // Mettez à jour les détails de la salle existante avec les détails de la nouvelle salle
                aExistingRoom.setRoomName(iNewRoom.getRoomName());
                aExistingRoom.setRoomStatue(iNewRoom.getRoomStatue());
                aExistingRoom.setRoomType(iNewRoom.getRoomType());
                aExistingRoom.setRoomCapacity(iNewRoom.getRoomCapacity());
                aExistingRoom.setRoomResponsible(iNewRoom.getRoomResponsible());
                aExistingRoom.setCleaningState(iNewRoom.getCleaningState());

                // Mettez à jour les autres champs au besoin

                try {
                    // Mettez à jour la salle dans la base de données
                    roomService.updateRoom(aExistingRoom);
                    return ("La chambre a été mis a jour avec succeés ! ");
                } catch (Exception e) {
                    // Gérez l'erreur de manière appropriée (par exemple, journalisez l'erreur, renvoyez une réponse d'erreur)
                    return ("Echéc de mise à  jour " + e.getMessage());
                }
            } else {
                // Salle avec le roomKey donné non trouvée
                return ("la chambre n'existe pas !");
            }
        } else {
            return ("L'unité de soin n'existe pas ");
        }
    }


    //// DELETE  A ROOM fROM A CAREUNIT
    @DeleteMapping("/{id}/deleteRoom/{roomkey}")
    public String deleteRoomFromCareUnit(@PathVariable("id") Integer iCareUnitKey, @PathVariable("roomkey") Integer iRoomKey) {
        try {
            Optional<CareUnit> aCareUnitOptional = careUnitService.getCareUnitByKey(iCareUnitKey);

            if (aCareUnitOptional.isPresent()) {
                CareUnit aCareUnit = aCareUnitOptional.get();

                // Recherche de la salle par son ID
                Optional<Room> aRoomOptional = aCareUnit.getRooms().stream()
                        .filter(room -> room.getRoomKey().equals(iRoomKey))
                        .findFirst();

                if (aRoomOptional.isPresent()) {
                    Room aRoom = aRoomOptional.get();
                    // Suppression de la salle de l'unité de soins
                    aCareUnit.getRooms().remove(aRoom);
                    // Suppression de la salle de l'unité de soins
                    roomService.deleteRoom(aRoom);

                    // Mettez à jour l'unité de soins dans la base de données
                    careUnitService.updateCareUnit(aCareUnit);

                    return ("La chambre a été supprimé avec succées !");
                } else {
                    // La salle avec l'ID donné n'a pas été trouvée dans l'unité de soins
                    return ("La chambre n'existe pas dans cette unité ");
                }
            } else {
                // L'unité de soins avec l'ID donné n'a pas été trouvée
                return ("L'unite de soin n'existe pas!");
            }
        } catch (Exception e) {
            return (e.getMessage());
        }
    }


    /////////    THIRD PART : CAREUNIT AND EQUIPMENTS ////////////////


    // ADD EQUIPMENT TO CAREUNIT
    @PostMapping("/{careUnitId}/equipments")
    public String addEquipmentToCareUnit(@PathVariable("careUnitId") Integer iCareUnitId, @RequestBody List<Long> iEquipmentList) {
        try {
            // Récupérer l'unité de soin par son ID
            Optional<CareUnit> aCareUnitOptional = careUnitService.getCareUnitByKey(iCareUnitId);
            if (aCareUnitOptional.isEmpty()) {
                return ("Aucune unité de soin existe avec cet ID !");
            }

            CareUnit aCareUnit = aCareUnitOptional.get();

            // Parcourir la liste des IDs d'équipements et les associer à l'unité de soin
            for (Long aEquipmentId : iEquipmentList) {
                // Récupérer l'équipement par son ID
                Optional<Equipment> aEquipmentOptional = equipmentService.getEquipmentById(aEquipmentId);
                if (aEquipmentOptional.isEmpty()) {
                    return ("Aucun equipemnt existe avec cet ID");
                }

                Equipment aEquipment = aEquipmentOptional.get();

                // Créer une association entre l'unité de soin et l'équipement
                CareUnitEquipLink aAssociation = new CareUnitEquipLink();
                aAssociation.setCareUnit(aCareUnit);
                aAssociation.setEquipment(aEquipment);

                // Enregistrer l'association
                careUnitService.addEquipmentToCareUnit(aAssociation);


            }
            return ("les Equipements ont été ajouté succées !");
        } catch (Exception e) {
            return (e.getMessage());
        }
    }


    // GET ALL THE  EQUIPMENTS FROM CAREUNIT
    @GetMapping("/{careUnitId}/equipments")
    public Set<CareUnitEquipLink> getEquipmentsByCareUnit(@PathVariable("careUnitId") Integer iCareUnitId) {
        Optional<CareUnit> aOptionalCareUnit = careUnitService.getCareUnitByKey(iCareUnitId);
        if (aOptionalCareUnit.isPresent()) {
            return careUnitService.getEquipmentsByCareUnit(iCareUnitId);
        } else {
            throw new IllegalStateException("Aucune unité existe avec cet ID ");
        }

    }


    // UPDATE EQUIPMENTS IN CAREUNIT
    @PutMapping("/{careUnitId}/updateEquipment")
    public ResponseEntity<String> updateEquipmentInCareUnitPost(@PathVariable("careUnitId") Integer iCareUnitId, @RequestBody UpdateEquipmentRequest iRequest) {
        try {
            careUnitService.updateEquipmentInCareUnit(iCareUnitId, iRequest.getOldEquipmentIds(), iRequest.getNewEquipmentIds());
            return new ResponseEntity<>("L'equipement a été mis a jour avec succées ", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Echéc de mis a jour cet equipement " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // DELETE EQUIPMENT FROM CAREUNIT

    @DeleteMapping("/{careUnitId}/deleteequipement/{equipmentId}")
    public String removeEquipmentFromCareUnit(@PathVariable("careUnitId") Integer iCareUnitId, @PathVariable("equipmentId") Long iEquipmentList) {
        try {
            careUnitService.removeEquipmentFromCareUnit(iCareUnitId, iEquipmentList);
            return ("L'equipement a été supprimé avec succées ");
        } catch (IllegalArgumentException e) {
            return ("Echéc de supprimer un equipement " + e.getMessage());
        }
    }



    @GetMapping("/getRooms")
    public ResponseEntity<?> getAllRooms(@RequestParam Integer careunitKey,
                                                  @RequestParam RoomType roomType,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date bedLocked_PlannedUnxTmBgn,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date bedLocked_PlannedUnxTmEnd,
                                                  @RequestParam double patientWeight,
                                                    @RequestParam boolean accompagnement
    ) {
        try {
            List<Room> aRoomList = careUnitService.getRoomsInCareUnit(careunitKey, roomType, bedLocked_PlannedUnxTmBgn, bedLocked_PlannedUnxTmEnd, patientWeight,accompagnement);
            if (!aRoomList.isEmpty()) {
                return new ResponseEntity<>(aRoomList, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune chambre disponible pour les critères spécifiés.");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }














  /*  @PostMapping("/saveBedLockedWithAccompagnant")
    public ResponseEntity<String> saveBedLockedWithAccompagnant(@RequestParam Integer roomKey, @RequestBody CompanionAndBedLockRequest request) {
        try {
            RoomC accompagnant = request.getCompanion();
            roomCompanionService.addCompanion(accompagnant);
            Integer accompagnantId = accompagnant.getRoomCompanionKy();

            BedLocked bedLocked = request.getBedLocked();
            bedLocked.setBedLockedOccupantKy(accompagnantId);

            ResponseEntity<Room> response = restTemplate.getForEntity(paramServiceUrl + "/{id}", Room.class, roomKey);

            if (response.getStatusCode().is2xxSuccessful()) {
                Room room = response.getBody();
                List<Bed> bedList = room.getRoomBed();

                for (Bed bed : bedList) {
                    if (bed.getBedType() == BedType.Simple) {
                        bedLocked.setBed(bed);
                        ResponseEntity<BedLocked> bedLockedResponse = restTemplate2.postForEntity(paramBedLockedUrl, bedLocked, BedLocked.class);



                        if (!bedLockedResponse.getStatusCode().is2xxSuccessful()) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'enregistrement de BedLocked.");
                        }

                        bed.setBedStatue(BedStatus.Reserve);
                        bed.setRoomBed(room);
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<Bed> requestEntity = new HttpEntity<>(bed, headers);
                        ResponseEntity<String> bedResponseEntity = restTemplate.postForEntity(paramBedUrl, requestEntity, String.class);

                        if (!bedResponseEntity.getStatusCode().is2xxSuccessful()) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la sauvegarde du lit.");
                        }
                    }
                }

                return ResponseEntity.ok("Accompagnant a été enregistré avec succès.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La chambre avec l'ID " + roomKey + " n'a pas été trouvée.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'enregistrement de BedLocked avec accompagnant: " + e.getMessage());
        }
    }
*/

}




