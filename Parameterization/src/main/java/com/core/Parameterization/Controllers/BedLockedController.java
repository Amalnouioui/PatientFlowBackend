package com.core.Parameterization.Controllers;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.*;
import com.core.Parameterization.Respositories.BedLockedRepository;
import com.core.Parameterization.Respositories.RoomRepository;
import com.core.Parameterization.Services.BedLockedService;
import com.core.Parameterization.Services.RoomCompanionService;
import com.core.Parameterization.Services.RoomService;
import com.core.patient.entities.Historique;
import com.core.patient.entities.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/patientAddmission")
@CrossOrigin(origins = "http://localhost:4200")


public class BedLockedController {
    @Autowired
private BedLockedService bedLockedService;
@Autowired
private BedLockedRepository bedLockedRepository;

@Autowired
private RoomService roomService;
@Autowired
private RoomCompanionService roomCompanionService;

    ///////////////////////////Perfect /////////////
    @PostMapping("/addBed")
    public String addPerson(@RequestParam Integer roomKey,
                            @RequestBody BedLocked bedLocked,
                            @RequestParam boolean accompagnement,
                            @RequestParam double patientWeight
    ) {
        try {
            bedLockedService.adPerssonToBed(roomKey, bedLocked,accompagnement, patientWeight);

            return "Patient affecteé avec succées !";
        }
        catch (IllegalStateException e) {
            return (e.getMessage());
        }
    }








  /*  @PostMapping("/saveBedLocked")
    public BedLocked saveBedLocked(@RequestBody BedLocked iBedLocked) {
        try {
            // Enregistrement du BedLocked en base de données
            bedLockedService.saveBedLocked(iBedLocked);
            // Retourne l'objet iBedLocked après l'avoir enregistré en base de données
            return iBedLocked;
        } catch (Exception e) {
            e.printStackTrace(); // Pour le débogage, affichez l'erreur
            return null; // Retourne null en cas d'erreur
        }
    }
    @PostMapping("/addBedLocked")
    public String addBed(@RequestBody BedLocked iBedLocked) {
        try {
            // Enregistrement du BedLocked en base de données
            bedLockedService.saveBedLocked(iBedLocked);
            // Retourne l'objet iBedLocked après l'avoir enregistré en base de données
            return ("Le lit a ete ajouter avec succées");
        } catch (Exception e) {
            e.printStackTrace(); // Pour le débogage, affichez l'erreur
            return null; // Retourne null en cas d'erreur
        }
    }
*/
    @GetMapping("getbyId/{id}")
    public ResponseEntity<?> getBedLockedById(@PathVariable("id") Integer iBedLockedOccupantKey) {
        try {
            BedLocked aBedLocked = bedLockedService.getBedLockedByKey(iBedLockedOccupantKey);
            if (aBedLocked != null) {
                return ResponseEntity.ok(aBedLocked);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("updatePatient/{id}")
    public String updateBedLockedDates(@PathVariable("id") Integer id, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date bedLocked_RealUnxTmBgn) {


        try {
            bedLockedService.changePatientReakEnteryDate(id,bedLocked_RealUnxTmBgn );
            return("La date réel d'entré a été modifier avec sucées !");
        } catch (Exception e) {
            return (e.getMessage());
        }



    }


    @GetMapping("/checkPatient/{id}")
    public boolean isInnBedLocked(@PathVariable("id") Integer id){
        boolean isInBedLocked=bedLockedService.patientCheck(id);
        if(isInBedLocked){
            return true;
        }else{
            return false ;
        }
    }
    String historySave = "http://localhost:8091/historique";
    RestTemplate restTemplate = new RestTemplate();

    //String url = "http://localhost:8091/patients/" + patientKey;
    RestTemplate restTemplate2 = new RestTemplate();
/*  @PostMapping("/changePatient")
    public ResponseEntity<String> deplacerPatient (@RequestParam Integer newroomKey,
                                                   @RequestBody BedLocked newBedLocked,
                                                   @RequestParam boolean accompagnement,
                                                   @RequestParam double patientWeight) {

        try {
            // Initialise les dates de placement à null
            Date realenteryDate = null;
            Date realexitDate = null;

            // Recherche de l'ancien lit du patient
          //  BedLocked oldbdelocked = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(newBedLocked.getBedLockedKy(), OccupantType.Patient);
           BedLocked  oldbdelocked =bedLockedService.getBedLockedByKey(newBedLocked.getBedLockedOccupantKy());
        System.out.println(oldbdelocked.getBedLockedKy());
            // Vérifie si l'ancien lit existe
            if (oldbdelocked != null) {
                // Obtient l'ancien lit
                Bed oldBed = oldbdelocked.getBed();


                System.out.println(oldBed);


                // Récupère les dates de début et de fin de la période de placement
                realenteryDate = oldbdelocked.getBedLocked_RealUnxTmBgn();
                realexitDate = oldbdelocked.getBedLocked_RealUnxTmEnd();

                // Supprime l'ancien lit de la base de données
                bedLockedRepository.delete(oldbdelocked);

                // Vérifie si l'ancien lit existe toujours après suppression
                if (oldBed != null) {
                    // Effectue le changement de lit du patient vers le nouveau lit
                    Bed newBed = bedLockedService.changePatientBed(newroomKey, newBedLocked, accompagnement, patientWeight);
                    // Mise à jour des dates de début et de fin de la période de placement du nouveau lit
                    if (realenteryDate != null || realexitDate != null) {
                        newBedLocked.setBedLocked_RealUnxTmBgn(realenteryDate);
                        newBedLocked.setBedLocked_RealUnxTmEnd(realexitDate);

                        bedLockedRepository.save(newBedLocked);
                    }
                    System.out.println(newBed);

                    // Récupère l'ancienne chambre et l'ancienne unité de soins associées à l'ancien lit
                    Room OldRoom = oldBed.getRoomBed();
                    CareUnit oldCareunit = OldRoom.getCareunitRoom();

                    // Récupère la nouvelle chambre spécifiée
                    Optional<Room> roomOptional = roomService.getRoomByKey(newroomKey);
                    if (roomOptional.isPresent()) {
                        Room NewRoom = roomOptional.get();
                        CareUnit NewCareUnit = NewRoom.getCareunitRoom();

                        // Vérifie si le lit a été changé
                        if (newBed.getBedKey() != oldBed.getBedKey()) {
                            // Crée un objet Historique pour enregistrer le changement de lit
                            Historique historique = new Historique();
                            historique.setOldBed(oldBed.getBedNumber());
                            historique.setNewBed(newBed.getBedKey());
                            historique.setNewCareUnit(NewCareUnit.getCareunitName());
                            historique.setOldCareUnit(oldCareunit.getCareunitName());
                            historique.setNewRoom(NewRoom.getRoomKey());
                            historique.setOldRoom(OldRoom.getRoomKey());

                            Date date = new Date();
                            historique.setPlacementDate(date);
                            Integer patientKey = newBedLocked.getBedLockedOccupantKy();



                            // Envoie une demande pour récupérer les détails du patient lié au nouveau lit
                            String getPatientUrl = "http://localhost:8091/patients/" + patientKey;
                            ResponseEntity<Patient> responseEntity = restTemplate.getForEntity(getPatientUrl, Patient.class);
                            Patient patient = responseEntity.getBody();


                            System.out.println(patient);
                            historique.setPatient(patient);


                            ResponseEntity<String> response = restTemplate.postForEntity(historySave, historique, String.class);
                            System.out.println(response);
                            if (response.getStatusCode() == HttpStatus.OK) {
                                System.out.println("Historique enregistré avec succès.");
                            } else {
                                System.err.println("Erreur lors de l'enregistrement de l'historique : " + response.getBody());
                            }

                        }
                    }

                    // Mise à jour des dates de début et de fin de la période de placement du nouveau lit
                    if (realenteryDate != null || realexitDate != null) {
                        newBedLocked.setBedLocked_RealUnxTmBgn(realenteryDate);
                        newBedLocked.setBedLocked_RealUnxTmEnd(realexitDate);
                    }

                    // Retourne un message de succès
                    return ResponseEntity.ok("Patient deplacé avec succès !");
                } else {
                    // Si l'ancien lit n'existe pas après suppression, lance une exception
                    throw new IllegalStateException("L'ancien lit n'existe pas.");
                }
            } else {
                // Si l'ancien lit n'existe pas, lance une exception
                throw new IllegalArgumentException("Le patient n'est pas affecté a un lit");
            }
        } catch (Exception e) {
            // Si une exception est levée pendant le traitement, retourne un message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du déplacement du patient : " + e.getMessage());
        }
    }
    .*/

    @PostMapping("/changePatient")
    public ResponseEntity<String> deplacerPatient (@RequestParam Integer newroomKey,
                                                   @RequestBody BedLocked newBedLocked,
                                                   @RequestParam boolean accompagnement,
                                                   @RequestParam double patientWeight
                                                  // @RequestParam RoomCompanion roomCompanion
    ) {

        try {
            // Initialise les dates de placement à null
            Date realenteryDate = null;
            Date realexitDate = null;

            // Recherche de l'ancien lit du patient
            //  BedLocked oldbdelocked = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(newBedLocked.getBedLockedKy(), OccupantType.Patient);


            BedLocked  oldbdelocked =bedLockedService.getBedLockedByKey(newBedLocked.getBedLockedOccupantKy());
            System.out.println(oldbdelocked.getBedLockedKy());
           // System.out.println(oldbdelocked.getBed());
            // Vérifie si l'ancien lit existe
            if (oldbdelocked != null) {
                // Obtient l'ancien lit
                Bed oldBed = oldbdelocked.getBed();
                System.out.println("oldBed" + oldBed);
                Room OldRoom = oldBed.getRoomBed();
                System.out.println("OldRoom" + OldRoom);
              RoomCompanion roomCompanion;
                Integer accompanionKey=null;
///Supression de l'accompagnant
                if (OldRoom.getRoomType() == RoomType.Double) {
                    for (Bed abedType : OldRoom.getRoomBed()) {
                        if (abedType.getBedType() == BedType.Simple) {

                            if (bedLockedService.checkAccompagnant(newBedLocked.getBedLockedOccupantKy())) {
                               accompanionKey = bedLockedService.getAccompagnat(newBedLocked.getBedLockedOccupantKy());
                                if (accompanionKey != null) {
                                    System.out.println("Companion Key " + accompanionKey);
                                    Optional<RoomCompanion> companion = roomCompanionService.getCompanionById(accompanionKey);

                                    if (companion.isPresent()) {

                                         roomCompanion=companion.get();
                                         System.out.println("L'accompagnant est "+roomCompanion);

                                        BedLocked companionBedLock = bedLockedService.getBedLockedcompanion(accompanionKey);
                                        Bed accompagnantBed=companionBedLock.getBed();
                                        accompagnantBed.setBedStatue(BedStatus.Disponible);
                                        accompagnantBed.setBedCleaningStatus(BedCleaningStatus.A_Nettoyer);
                                        System.out.println("Companion BedLocked" + companionBedLock);
                                        bedLockedRepository.delete(companionBedLock);

                                    }

                                }

                            }



                        }

                    }


                }
               // throw new IllegalStateException("la chambre n'est pas de type Double ");




                System.out.println("Old patientBed"+oldBed);



                // Récupère les dates de début et de fin de la période de placement
                realenteryDate = oldbdelocked.getBedLocked_RealUnxTmBgn();
                realexitDate = oldbdelocked.getBedLocked_RealUnxTmEnd();
oldBed.setBedStatue(BedStatus.Disponible);
oldBed.setBedCleaningStatus(BedCleaningStatus.A_Nettoyer);
                // Supprime l'ancien lit de la base de données
                bedLockedRepository.delete(oldbdelocked);

                // Vérifie si l'ancien lit existe toujours après suppression
                if (oldBed != null) {
                    // Effectue le changement de lit du patient vers le nouveau lit
                    Bed newBed = bedLockedService.changePatientBed(newroomKey, newBedLocked, accompagnement, patientWeight,accompanionKey);
                    // Mise à jour des dates de début et de fin de la période de placement du nouveau lit
                    if (realenteryDate != null || realexitDate != null) {
                        newBedLocked.setBedLocked_RealUnxTmBgn(realenteryDate);
                        newBedLocked.setBedLocked_RealUnxTmEnd(realexitDate);

                        bedLockedRepository.save(newBedLocked);
                    }
                    System.out.println(newBed);

                    // Récupère l'ancienne chambre et l'ancienne unité de soins associées à l'ancien lit

                    CareUnit oldCareunit = OldRoom.getCareunitRoom();

                    // Récupère la nouvelle chambre spécifiée
                    Optional<Room> roomOptional = roomService.getRoomByKey(newroomKey);
                    if (roomOptional.isPresent()) {
                        Room NewRoom = roomOptional.get();
                        CareUnit NewCareUnit = NewRoom.getCareunitRoom();

                        // Vérifie si le lit a été changé
                        if (newBed.getBedKey() != oldBed.getBedKey()) {
                            // Crée un objet Historique pour enregistrer le changement de lit
                            Historique historique = new Historique();
                            historique.setOldBed(oldBed.getBedNumber());
                            historique.setNewBed(newBed.getBedKey());
                            historique.setNewCareUnit(NewCareUnit.getCareunitName());
                            historique.setOldCareUnit(oldCareunit.getCareunitName());
                            historique.setNewRoom(NewRoom.getRoomKey());
                            historique.setOldRoom(OldRoom.getRoomKey());

                            Date date = new Date();
                            historique.setPlacementDate(date);
                            Integer patientKey = newBedLocked.getBedLockedOccupantKy();



                            // Envoie une demande pour récupérer les détails du patient lié au nouveau lit
                            String getPatientUrl = "http://localhost:8091/patients/" + patientKey;
                            ResponseEntity<Patient> responseEntity = restTemplate.getForEntity(getPatientUrl, Patient.class);
                            Patient patient = responseEntity.getBody();


                            System.out.println(patient);
                            historique.setPatient(patient);


                            ResponseEntity<String> response = restTemplate.postForEntity(historySave, historique, String.class);
                            System.out.println(response);
                            if (response.getStatusCode() == HttpStatus.OK) {
                                System.out.println("Historique enregistré avec succès.");
                            } else {
                                System.err.println("Erreur lors de l'enregistrement de l'historique : " + response.getBody());
                            }

                        }
                    }

                    // Mise à jour des dates de début et de fin de la période de placement du nouveau lit
                    if (realenteryDate != null || realexitDate != null) {
                        newBedLocked.setBedLocked_RealUnxTmBgn(realenteryDate);
                        newBedLocked.setBedLocked_RealUnxTmEnd(realexitDate);
                    }

                    // Retourne un message de succès
                    return ResponseEntity.ok("Patient deplacé avec succès !");
                } else {
                    // Si l'ancien lit n'existe pas après suppression, lance une exception
                    throw new IllegalStateException("L'ancien lit n'existe pas.");
                }
            } else {
                // Si l'ancien lit n'existe pas, lance une exception
                throw new IllegalArgumentException("Le patient n'est pas affecté a un lit");
            }
        } catch (Exception e) {
            // Si une exception est levée pendant le traitement, retourne un message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du déplacement du patient : " + e.getMessage());
        }
    }
@GetMapping("/getPatientRoom/{patientKey}")
    public Room getPatientRoom(@PathVariable("patientKey")Integer patientKey){
        return bedLockedService.getPatientRoom(patientKey);
}

@GetMapping("checkAccompagnant/{patientKey}")
public boolean isAccompanied(@PathVariable("patientKey") Integer patientKey){
       return  bedLockedService.checkAccompagnant(patientKey);
}

@GetMapping("getAccmopagnant/{patientKey}")
        public Integer getAccompanion (@PathVariable("patientKey") Integer patientKey){
    return  bedLockedService.getAccompagnat(patientKey);
}

}


