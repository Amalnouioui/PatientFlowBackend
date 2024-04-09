package com.core.Parameterization.Controllers;


import aj.org.objectweb.asm.TypeReference;
import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.RoomStatus;
import com.core.Parameterization.Entities.Enumeration.UnitStatus;
import com.core.Parameterization.Entities.Enumeration.UnitType;
import com.core.Parameterization.Respositories.CareUnitServiceLinkRepo;
import com.core.Parameterization.Services.*;
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
@RequestMapping("/parameterization/careunit")
public class CareUnitController {
    @Autowired
    private  CareUnitService careUnitService;
    @Autowired
     private RoomService roomService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ServiceSer serviceSer;

    @Autowired
    private CareUnitServiceLinkService careUnitServiceLinkService;


    //ADD EQUIPMENT AND SERVICE TO CAREUNIT WHEN ADDING NEW CAREUNIT
    @PostMapping

    public String createCareUnit(@RequestBody Map<String, Object> iRequestBody){
        try{
            // Extraire les données de l'unité de soin du JSON
            CareUnit aCareUnit = extractCareUnitFromJson(iRequestBody);

            // Enregistrer l'unité de soin dans la base de données
            CareUnit aCareUnitService = careUnitService.create(aCareUnit);

            // Extraire les IDs des équipements du JSON
            List<Long> aEquipmentList = extractEquipmentIdsFromJson(iRequestBody);
            List<Long> aServiceList = extractServiceIdsFromJson(iRequestBody);

            // Ajouter les équipements à l'unité de soin
            addEquipmentsAndServicesToCareUnit(aCareUnit, aEquipmentList, aServiceList);

            return  ("l'unité de soin a été ajouté avec succées");
        }catch(Exception e){
            return  (e.getMessage());
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
    //EXTRACT THE SERVICE LIST  FROM THE BODY
    private List<Long>extractServiceIdsFromJson(Map<String, Object> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> aServiceList = (List<Integer>) requestBody.get("serviceList");
        if (aServiceList != null) {
            return aServiceList.stream().map(Long::valueOf).collect(Collectors.toList());
        } else {
            return Collections.emptyList();

        }

    }

    //ADD CAREUNIT , EQUIPMENT AND SERVICE
    private void addEquipmentsAndServicesToCareUnit(CareUnit iCareUnit, List<Long> iEquipmentList, List<Long> iServiceList) {
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


       // Ajouter les services à l'unité de soin
       for (Long aServiceId : iServiceList) {
           Optional<ServiceEntity> aServiceOptional = serviceSer.getServiceById(aServiceId);
           if (aServiceOptional.isPresent()) {
               ServiceEntity aService = aServiceOptional.get();
               // Créer une association entre l'unité de soin et le service
               CareUnitServiceLink aAssociation = new CareUnitServiceLink();
               aAssociation.setCareUnit(iCareUnit);
               aAssociation.setService(aService);
               // Enregistrer l'association dans la base de données
               careUnitService.addServices(aAssociation);
           } else {
               throw new IllegalStateException("Le Service avec l'ID " + aServiceId + " n'existe pas.");
           }
       }
   }


    // RETRIVE ALL CAREUNITS
    @GetMapping

    public ResponseEntity <List<CareUnit>> getAllCareUnit(){
    try
    {
            List<CareUnit> aAllCareUnits =careUnitService.retreivesAllCareUnits();

            return new ResponseEntity<>(aAllCareUnits,HttpStatus.OK);
    }catch(Exception e){
        return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    //RETREIVE CAREUNIT BY ITS ID
    @GetMapping("/{id}")
        public ResponseEntity<CareUnit>getCareUnitById(@PathVariable ("id") Integer iCareunitKey){
            Optional<CareUnit>aCareUnit=careUnitService.getCareUnitByKey(iCareunitKey);
            return aCareUnit.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));


        }

    // RETREIVE CAREUNIT BY ITS NAME

    @GetMapping("/careunitName/{careunitName}")
    public ResponseEntity<CareUnit>getCareUnitByName(@PathVariable ("careunitName")String iCareunitName){
        CareUnit aCareUnit=careUnitService.getCareUnitbyNme(iCareunitName);
    if (aCareUnit!=null){
            return  new ResponseEntity<>(aCareUnit,HttpStatus.OK);
        }else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    // RETREIVE CAREUNITS BY THEIR STATUS
    @GetMapping("/careunitStatue/{careunitStatue}")
            public ResponseEntity<List<CareUnit>>getCareUnitStatue(@PathVariable("careunitStatue") UnitStatus iCareunitStatue){
        List<CareUnit> aCareUnit=careUnitService.getCareUnitByStatue(iCareunitStatue);
        if (aCareUnit!=null){
            return  new ResponseEntity<>(aCareUnit,HttpStatus.OK);
        }else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }}


    // UPDATE CAREUNIT
    @PutMapping("/changeCareUnit/{careunitKey}")
    public String ChangeCareUnit(@PathVariable("careunitKey") Integer iCareunitKey, @RequestBody CareUnit iNewCareUnit) {
        try {
            careUnitService.update(iCareunitKey, iNewCareUnit);
            return("l'unite de soin a été mis a jour avec succées ");
        } catch (Exception e) {
            return (e.getMessage());
        }
    }



   //DELETE CAREUNIT WITH THE EQUIPMENT AND SERVICE ASSOCIATION
    @DeleteMapping("/{id}")
    public String deleteCareUnitAndAssociatedEntities(@PathVariable("id") Integer iId) {
        try{
        Optional<CareUnit> aExistingCareUnitOptional = careUnitService.getCareUnitByKey(iId);
            CareUnit aExistingCareUnit = aExistingCareUnitOptional.get();

            // Remove associated Equipments
            for (CareUnitEquipLink aAssociation : aExistingCareUnit.getCareUnitEquipLinkSet()) {
                careUnitService.removeEquipmentFromCareUnit(aExistingCareUnit.getCareunitKey(), aAssociation.getEquipment().getEquipementkey());
            }

            // Remove associated Services
            for (CareUnitServiceLink aAssociation : aExistingCareUnit.getCareUnitServiceLinkSet()) {
                // Assuming you have a method to remove a Service from a CareUnit
                careUnitService.removeService(aExistingCareUnit.getCareunitKey(), aAssociation.getService().getServiceKy());
            }

            // Delete the CareUnit
            careUnitService.delete(iId);

            return ("L'unite de soin a été supprimé avec succées ");
            }catch(Exception e){
            return (e.getMessage());}}



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
            Optional<ServiceEntity> serviceEntity = serviceSer.getServiceById(iRoom.getService().getServiceKy());

            if (aCareUnit.isPresent() && serviceEntity.isPresent()) {
                // Vérifiez si le type d'unité est Cherugie
                if (aCareUnit.get().getCareuniType() == UnitType.Cherugie) {
                    // Vérifiez si le service appartient à cette unité spécifique
                    for (CareUnitServiceLink link : aCareUnit.get().getCareUnitServiceLinkSet()) {
                        if (link.getService().getServiceKy().equals(serviceEntity.get().getServiceKy())) {
                            // Associez la chambre au service et ajoutez-la à l'unité de soin
                            iRoom.setService(serviceEntity.get());
                            careUnitService.addRoom(aCareUnit.get(), iRoom);
                            return "La chambre a été ajoutée avec succès !";
                        }
                    }
                    return "Le service spécifié n'appartient pas à cette unité de chirurgie !";
                } else {
                    return "Vous ne pouvez ajouter une chambre qu'à une unité de chirurgie !";
                }
            } else {
                return "Aucune unité de soin ou service existe avec cet ID !";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }



    



    // RETREIVE ALL ROOMS ASSOCIATED WITH CAREUNIT
    @GetMapping("/{id}/GetRoom")
    public ResponseEntity<List<Room>>GetRoomListByCareUnit(@PathVariable("id")Integer iCareunitKey){
        Optional<CareUnit>aCareUnit=careUnitService.getCareUnitByKey(iCareunitKey);
        if(aCareUnit.isPresent()){
            List<Room>aRooms=careUnitService.getRoom(aCareUnit.get());
            return new ResponseEntity<>(aRooms,HttpStatus.OK);
        }else{
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


    /////RETREIVE  ROOM BY STATUS IN A SPECIFIC  CEUNIT
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
    public  String updateRoomInCareUnit(
            @PathVariable("careunitKey") Integer iCareunitKey,
            @PathVariable("roomKey") Integer iRoomKey,
            @RequestBody Room iNewRoom) {

        Optional<CareUnit> aCareUnitOptional = careUnitService.getCareUnitByKey(iCareunitKey);
        if(aCareUnitOptional.isPresent()) {
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
                    return("Echéc de mise à  jour "+e.getMessage());
                }
            } else {
                // Salle avec le roomKey donné non trouvée
                return("la chambre n'existe pas !");
            }
        } else {
            return("L'unité de soin n'existe pas ");
        }
    }


    //// DELETE  A ROOM fROM A CAREUNIT
    @DeleteMapping("/{id}/deleteRoom/{roomkey}")
    public String deleteRoomFromCareUnit(@PathVariable("id") Integer iCareUnitKey, @PathVariable("roomkey") Integer iRoomKey) {
        try{
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
        }}catch (Exception e){
            return (e.getMessage());
        }
    }


    /////////    THIRD PART : CAREUNIT AND EQUIPMENTS ////////////////



    // ADD EQUIPMENT TO CAREUNIT
    @PostMapping("/{careUnitId}/equipments")
    public String addEquipmentToCareUnit(@PathVariable("careUnitId") Integer iCareUnitId, @RequestBody List<Long> iEquipmentList) {
        try{
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
    }catch(Exception e){
            return (e.getMessage());
        }
    }



    // GET ALL THE  EQUIPMENTS FROM CAREUNIT
    @GetMapping("/{careUnitId}/equipments")
    public Set<CareUnitEquipLink> getEquipmentsByCareUnit(@PathVariable("careUnitId") Integer iCareUnitId) {
        Optional<CareUnit> aOptionalCareUnit = careUnitService.getCareUnitByKey(iCareUnitId);
        if (aOptionalCareUnit.isPresent()) {
            return careUnitService.getEquipmentsByCareUnit(iCareUnitId);
        }else{
            throw  new IllegalStateException("Aucune unité existe avec cet ID ");
        }

    }



   // UPDATE EQUIPMENTS IN CAREUNIT
    @PutMapping("/{careUnitId}/updateEquipment")
    public ResponseEntity<String> updateEquipmentInCareUnitPost(@PathVariable("careUnitId") Integer iCareUnitId, @RequestBody UpdateEquipmentRequest iRequest) {
        try {
            careUnitService.updateEquipmentInCareUnit(iCareUnitId, iRequest.getOldEquipmentIds(), iRequest.getNewEquipmentIds());
            return new ResponseEntity<>("L'equipement a été mis a jour avec succées ", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Echéc de mis a jour cet equipement "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // DELETE EQUIPMENT FROM CAREUNIT

    @DeleteMapping("/{careUnitId}/deleteequipement/{equipmentId}")
    public String removeEquipmentFromCareUnit(@PathVariable("careUnitId") Integer iCareUnitId, @PathVariable("equipmentId") Long iEquipmentList) {
        try {
            careUnitService.removeEquipmentFromCareUnit(iCareUnitId, iEquipmentList);
            return ("L'equipement a été supprimé avec succées ");
        } catch (IllegalArgumentException e) {
            return ("Echéc de supprimer un equipement "+e.getMessage());
        }
    }



    //////////  FOURTH PART : CAREUNIT AND SERVICE ///////////


    // ADD SERVICE TO CAREUNIT

    @PostMapping("/{careUnitId}/services")
    public String addServicesToCareUnit(@PathVariable("careUnitId") Integer iCareUnitId, @RequestBody List<Long> iServiceList) {
       try{
        // Récupérer l'unité de soin par son ID
        Optional<CareUnit> careUnitOptional = careUnitService.getCareUnitByKey(iCareUnitId);
        if (careUnitOptional.isEmpty()) {
            return ("Aucune unité de soin existe avec cet ID");
        }

        CareUnit aCareUnit = careUnitOptional.get();
        List<String> errorMessages = new ArrayList<>();

        // Parcourir la liste des IDs d'équipements et les associer à l'unité de soin
        for (Long aServiceKey : iServiceList) {

            // Récupérer l'équipement par son ID
            Optional<ServiceEntity> aServiceEntityOptional = serviceSer.getServiceById(aServiceKey);
            if (aServiceEntityOptional.isEmpty()) {
               errorMessages.add("Aucun Service existe avec L'ID :"+aServiceKey);
                continue;
            }

            ServiceEntity aService = aServiceEntityOptional.get();



            List<CareUnitServiceLink> aCareunitServiceList = careUnitServiceLinkService.getCareUnitAndService(iCareUnitId, aServiceKey);
            if (!aCareunitServiceList.isEmpty()) {
               errorMessages.add("Le service avec l'ID " + aServiceKey + " existe déjà dans cette unité de soin !");
               continue;
            }
                // Créer une association entre l'tunité de soin et l'équipement
                CareUnitServiceLink aAssociation = new CareUnitServiceLink();
                aAssociation.setCareUnit(aCareUnit);
                aAssociation.setService(aService);
                // Enregistrer l'association
                careUnitService.addServices(aAssociation);

        }if (!errorMessages.isEmpty()){
               return "Échec d'ajouter des services : " + String.join(", ", errorMessages);
           }
           return "Les services ont été ajoutés avec succès !";
    }catch(Exception e ){
           return("Echéc d'ajouter des sevices : "+e.getMessage());
       }
    }


    // GET SERVICES FROM A SPECIFIC  CAREUNIT
    @GetMapping("/{careUnitId}/getServices")
    public Set<CareUnitServiceLink> getServicesByCareUnit(@PathVariable("careUnitId") Integer iCareUnitId) {
        Optional<CareUnit> aOptionalCareUnit = careUnitService.getCareUnitByKey(iCareUnitId);
        if (aOptionalCareUnit.isPresent()) {
            return careUnitService.getServicesByCareUnit(iCareUnitId);
        }else{
            throw  new IllegalStateException("Aucune unité de soin existe avec cet ID\"");
        }
    }


    // UPDATE SERVICE IN CAREUNIT

    @PutMapping("/{careUnitId}/ypdateService")
    public ResponseEntity<String> updateServiceICareUnit(@PathVariable("careUnitId") Integer iCareUnitId, @RequestBody UpdatedServiceRequest iRequest) {
        try {
            careUnitService.updateService(iCareUnitId, iRequest.getOldServiceId(), iRequest.getNewServiceId());
            return new ResponseEntity<>("Le service a été mis a jour avec succées", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Echec de mis a jour cet equipement "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // DELETE A SPECIFIC SERVICE FROM A SPECIFIC CAREUNIT

    @DeleteMapping("/{careUnitId}/deleteservice/{serviceKey}")
    public String removeServiceFromCareUnit(@PathVariable("careUnitId") Integer iCareUnitId, @PathVariable("serviceKey") Long iServiceKey) {
        try {
            careUnitService.removeService(iCareUnitId, iServiceKey);
            return ("Le setvice a été supprimé avec succées !");
        } catch (IllegalArgumentException e) {
            return ("Echec de supprimer le service"+e.getMessage());
        }
    }

}




