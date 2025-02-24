package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.Bed;
import com.core.Parameterization.Entities.BedLocked;
import com.core.Parameterization.Entities.Enumeration.*;
import com.core.Parameterization.Entities.Room;
import com.core.Parameterization.Entities.RoomCompanion;
import com.core.Parameterization.Respositories.BedLockedRepository;
import com.core.Parameterization.Respositories.BedRespository;
import com.core.Parameterization.Respositories.RoomRepository;
import com.core.Parameterization.Services.BedLockedService;
import com.core.Parameterization.Services.RoomCompanionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BedLockedImpl implements BedLockedService {
    @Autowired
    private BedLockedRepository bedLockedRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BedRespository bedRespository;
    @Autowired
    private RoomCompanionService roomCompanionService;

    @Override
    public void addPerson(BedLocked bedLocked) {


        bedLockedRepository.save(bedLocked);


    }
  /*  @Override
    public void adPerssonToBed(Integer roomKey, BedLocked bedLocked,boolean accompagnement) {
        Bed medicalBed = null;
        Bed simpleBed = null;

        Optional<Room> aRoom = roomRepository.findById(roomKey);
        if (aRoom.isPresent()) {
            if (aRoom.get().getRoomType() == RoomType.Double) {
                List<Bed> aBedList = aRoom.get().getRoomBed();
                if (accompagnement) {
                    // Si l'utilisateur est accompagné
                    // Recherchez un lit médicalisé pour le patient
                    for (Bed bed : aBedList) {
                        if (bed.getBedType() == BedType.Medicalise) {
                            medicalBed = bed;
                            break;
                        }
                    }
                    // Recherchez un lit simple pour l'accompagnant
                    List<Bed> simpleBeds = bedRespository.findByBedType(BedType.Simple);
                    if (!simpleBeds.isEmpty()) {
                        simpleBed = simpleBeds.get(0);
                    }
                } else {
                    // Si l'utilisateur n'est pas accompagné, affectez-le à un lit quelconque
                    if (!aBedList.isEmpty()) {
                        medicalBed = aBedList.get(0);
                    }
                }

                // Enregistrez les lits mis à jour dans la base de données
                if (medicalBed != null) {
                    Integer patientKey = bedLocked.getBedLockedOccupantKy();
                    BedLocked abedLocked = returnBedLockedbyPatientKey(patientKey);
                    medicalBed.setBed(abedLocked);
                    bedRespository.save(medicalBed);
                }
                if (simpleBed != null) {
                    // Enregistrez le lit simple pour l'accompagnant
                    Integer patientKey = bedLocked.getBedLockedOccupantKy();
                    BedLocked abedLocked = returnBedLockedbyPatientKey(patientKey);
                    simpleBed.setBed(abedLocked);
                    bedRespository.save(simpleBed);
                }
            }
        }


        BedLocked aBedLockedPerson=returnBedLockedbyPatientKey(bedLocked.getBedLockedOccupantKy());
        if(aBedLockedPerson!=null){
            throw new IllegalStateException("Cette Personne est déja affecter a un lit");
        }
        bedLockedRepository.save(bedLocked);

        Optional<Room> aRoomOptional = roomRepository.findById(roomKey);
        if (aRoomOptional.isPresent()) {
            List<Bed> bedList = aRoomOptional.get().getRoomBed();
            if (!bedList.isEmpty()) {
                Bed bed = bedList.get(0); // Obtenez le premier lit de la liste
                Integer patientKey = bedLocked.getBedLockedOccupantKy();
                BedLocked abedLocked = returnBedLockedbyPatientKey(patientKey);
                bed.setBed(abedLocked); // Mettez à jour la référence de lit bloqué
                bedRespository.save(bed); // Enregistrez les modifications dans la base de données
            }
        }
    }

    private BedLocked returnBedLockedbyPatientKey(Integer personKey) {
        return bedLockedRepository.findByBedLockedOccupantKy(personKey);
    }
*/



   /* @Override
    public void adPerssonToBed(Integer roomKey, CompanionAndBedLockRequest request, boolean accompagnement) {

        List<BedLocked> abedLockedList = request.getBedLocks();
        for (BedLocked bedLocked : abedLockedList) {
            if (isPatientAlreadyAssigned(bedLocked.getBedLockedOccupantKy())) {
                throw new IllegalStateException("Ce patient est déjà affecté à un lit.");
            }
            bedLockedRepository.save(bedLocked);

            Optional<Room> optionalRoom = roomRepository.findById(roomKey);
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                if (room.getRoomType() == RoomType.Double) {
                    List<Bed> beds = room.getRoomBed();
                    if (accompagnement) {
                        // Si accompagné, chercher un lit médicalisé pour le patient
                        Bed medicalBed = beds.stream()
                                .filter(bed -> bed.getBedType() == BedType.Medicalise)
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException("Pas de lit médicalisé disponible dans la chambre double"));

                        // Chercher un lit simple pour l'accompagnant
                        Bed simpleBed = beds.stream()
                                .filter(bed -> bed.getBedType() == BedType.Simple)
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException("Pas de lit simple disponible dans la chambre double"));

                        if (bedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                            // Affecter le patient au lit médicalisé
                            assignPersonToBed(bedLocked, medicalBed);
                        }else if (bedLocked.getBedLockedOccupantType() == OccupantType.Accompagnant)
                        // Affecter l'accompagnant au lit simple
                            assignPersonToBed(bedLocked, simpleBed);
                    }





                    else {

                        if (!beds.isEmpty()) {
                            Bed firstBed = beds.get(0);
                            assignPersonToBed(bedLocked, firstBed);
                        } else {
                            throw new IllegalStateException("Pas de lit disponible dans la chambre double");
                        }
                    }
                } else if (room.getRoomType() != RoomType.Double && !accompagnement) {
                    List<Bed> beds = room.getRoomBed();
                    Bed firstBed = beds.get(0);
                    assignPersonToBed(bedLocked, firstBed);
                } else {
                    throw new IllegalStateException("Pas de lit disponible dans la chambre " + room.getRoomType());
                }

            }
        }

    }
*/


    //////////////////////////


  /*  public void adPerssonToBed(Integer roomKey, BedLocked bedLocked, boolean accompagnement) {
        try {
            // Vérifiez d'abord si le patient est déjà affecté à un lit
            if (isPatientAlreadyAssigned(bedLocked.getBedLockedOccupantKy())) {
                throw new IllegalStateException("Ce patient est déjà affecté à un lit.");
            }

            // Enregistrez d'abord le verrou de lit
            bedLockedRepository.save(bedLocked);

            // Recherchez la chambre par son identifiant
            Optional<Room> optionalRoom = roomRepository.findById(roomKey);
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                List<Bed> beds = room.getRoomBed();

                // Logique pour une chambre double
                if (room.getRoomType() == RoomType.Double) {
                    if (accompagnement) {
                        long simpleBedCount = beds.stream().filter(bed -> bed.getBedType() == BedType.Simple).count();
                        long medicalBedCount = beds.stream().filter(bed -> bed.getBedType() == BedType.Medicalise).count();

                        if (simpleBedCount == 1 && medicalBedCount == 1) {
                            // Si la chambre contient un lit simple et un lit médicalisé, affectez le patient au lit médicalisé
                            Bed medicalBed = beds.stream()
                                    .filter(bed -> bed.getBedType() == BedType.Medicalise)
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalStateException("Pas de lit médicalisé disponible dans la chambre double"));
                            if (bedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                assignPersonToBed(bedLocked, medicalBed);
                            }
                        } else {
                            // Si la chambre contient d'autres configurations de lits, lancez une exception
                            throw new IllegalStateException("La chambre double ne contient pas la configuration attendue de lits");
                        }
                    } else {
                        // Logique pour une chambre non double (autres types de chambre)
                        if (!beds.isEmpty()) {
                            // Si la chambre contient des lits, attribuez le patient au premier lit disponible
                            assignPersonToFirstAvailableBed(bedLocked, beds);
                        } else {
                            throw new IllegalStateException("Pas de lit disponible dans la chambre " + room.getRoomType());
                        }
                    }
                } else {
                    throw new IllegalStateException("Chambre non trouvée avec l'ID: " + roomKey);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
*/

    //////////////////////////LAST VERSION ////////////////////////////
    @Transactional
    @Override
    public void adPerssonToBed(Integer roomKey, BedLocked bedLocked, boolean accompagnement, double patientWeight) {
        try {
            // Vérifiez d'abord si le patient est déjà affecté à un lit
            if (isPatientAlreadyAssigned(bedLocked.getBedLockedOccupantKy())) {
                throw new IllegalStateException("Ce patient est déjà affecté à un lit.");
            }

            // Enregistrez d'abord le verrou de lit
            bedLockedRepository.save(bedLocked);

            // Recherchez la chambre par son identifiant
            Optional<Room> optionalRoom = roomRepository.findById(roomKey);
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                List<Bed> beds = room.getRoomBed();

                // Logique pour une chambre double
                if (room.getRoomType() == RoomType.Double) {
                    if (accompagnement) {
                        long simpleBedCount = 0;
                        long medicalBedCount = 0;
                        for (Bed abed : beds) {
                            if (abed.getBedType() == BedType.Simple) {
                                simpleBedCount++;
                            } else if (abed.getBedType() == BedType.Medicalise) {
                                medicalBedCount++;
                            }
                        }

                        // Si la chambre contient exactement un lit simple et un lit médicalisé
                        if (simpleBedCount == 1 && medicalBedCount == 1) {
                            for (Bed abed : beds) {
                                if (abed.getBedType() == BedType.Medicalise && bedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                    assignPersonToBed(bedLocked, abed);
                                    return; // Sortir de la méthode après avoir attribué le patient au lit médicalisé
                                }
                            }
                        } else if (simpleBedCount == 2) {
                            // Si la chambre contient au moins deux lits simples, attribuer le patient à l'un d'eux
                            for (Bed abed : beds) {
                                if (abed.getBedType() == BedType.Simple && bedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                    assignPersonToBed(bedLocked, abed);
                                    return; // Sortir de la méthode après avoir attribué le patient à un lit simple
                                }
                            }
                        } else {
                            throw new IllegalStateException("La chambre double ne contient pas la configuration attendue de lits");
                        }
                    } else if (!accompagnement) {


                        Date patientEntry = bedLocked.getBedLocked_PlannedUnxTmBgn();
                        Date patientExit = bedLocked.getBedLocked_PlannedUnxTmEnd();
                        for (Bed abed : beds) {

                            if (isBedInBedLocked(abed)) {
                                if (checkdates(abed, patientEntry, patientExit) && verifyGoodRoomCondition(abed, patientWeight)) {
                                    assignPersonToBed(bedLocked, abed);
                                    return;

                                }
                                System.out.println("appartient a bedLocked " + isBedInBedLocked(abed));
                                System.out.println("reservation de lit" + checkdates(abed, patientEntry, patientExit));

                            } else if (!isBedInBedLocked(abed) && verifyGoodRoomCondition(abed, patientWeight)) {
                                assignPersonToBed(bedLocked, abed);
                                return;
                            }
                        }


                    }


                }


                // Logique pour une chambre collective
                else if (room.getRoomType() == RoomType.COLLECTIVE) {
                    Date patientEntry = bedLocked.getBedLocked_PlannedUnxTmBgn();
                    Date patientExit = bedLocked.getBedLocked_PlannedUnxTmEnd();
                    boolean bedFound = false;
                    for (Bed bed : beds) {

                        if (isBedInBedLocked(bed)) {

                            if (checkdates(bed, patientEntry, patientExit) && verifyGoodRoomCondition(bed, patientWeight)) {
                                assignPersonToBed(bedLocked, bed);
                                bedFound = true;
                                break; // Sortir de la boucle après avoir trouvé un lit disponible

                            }

                        } else if (!isBedInBedLocked(bed) && verifyGoodRoomCondition(bed, patientWeight)) {
                            assignPersonToBed(bedLocked, bed);
                            bedFound = true;
                            break; // Sortir de la boucle après avoir attribué le patient au lit
                        }


                    }
                    if (!bedFound) {
                        throw new IllegalStateException("Aucun lit disponible avec les bons critères !");
                    }
                } else {
                    // Logique pour une chambre non double (autres types de chambre)
                    if (!beds.isEmpty()) {
                        // Si la chambre contient des lits, attribuez le patient au premier lit disponible
                        assignPersonToFirstAvailableBed(bedLocked, beds);
                    } else {
                        throw new IllegalStateException("Pas de lit disponible dans la chambre " + room.getRoomType());
                    }
                }
            } else {
                throw new IllegalStateException("Chambre non trouvée avec l'ID: " + roomKey);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private boolean isBedInBedLocked(Bed aBed) {
        List<BedLocked> aBedBedLockeds = aBed.getBedLockeds();
        if (!aBedBedLockeds.isEmpty()) {
            return true;
        }
        return false;


    }

    private boolean checkdates(Bed aBed, Date newPatientEntryDate, Date newPatientExitDate) {
        // Vérifier si le lit est réservé dans la table bedLocked
        if (isBedInBedLocked(aBed)) {
            for (BedLocked abedLockeds : aBed.getBedLockeds()) {
                Date plannedUnxTmBgn = abedLockeds.getBedLocked_PlannedUnxTmBgn();
                Date plannedUnxTmEnd = abedLockeds.getBedLocked_PlannedUnxTmEnd();
                Date realUnxBgn = abedLockeds.getBedLocked_RealUnxTmBgn();
                Date realUnxEnd = abedLockeds.getBedLocked_RealUnxTmEnd();

                // Vérifier si les dates du nouveau patient chevauchent les dates de réservation du lit
                if ((plannedUnxTmEnd != null && plannedUnxTmBgn != null) || (realUnxBgn != null && realUnxEnd != null)) {
                    if ((newPatientEntryDate.before(plannedUnxTmEnd) && newPatientExitDate.after(plannedUnxTmBgn)) ||
                            (newPatientExitDate.after(plannedUnxTmBgn) && newPatientExitDate.after(newPatientEntryDate)) ||
                            (newPatientEntryDate.before(realUnxEnd) && newPatientExitDate.after(plannedUnxTmBgn)) ||
                            (newPatientExitDate.after(realUnxBgn) && newPatientExitDate.after(newPatientEntryDate))) {
                        // Conflit trouvé, retourner faux immédiatement
                        return false;
                    }
                }
            }

            // Aucun conflit trouvé, retourner vrai
            return true;
        }

        // Aucune réservation pour ce lit, donc aucun conflit
        return true;
    }


    // Méthode pour attribuer une personne à un lit disponible
    private void assignPersonToFirstAvailableBed(BedLocked bedLocked, List<Bed> beds) {
        Bed firstBed = beds.get(0);
        assignPersonToBed(bedLocked, firstBed);

    }


    // Méthode pour attribuer une personne à un lit spécifique
    private void assignPersonToBed(BedLocked bedLocked, Bed bed) {
        bedLocked.setBed(bed);
        bed.setBedStatue(BedStatus.Reserve);
        bedRespository.save(bed);
        bedLockedRepository.save(bedLocked);
    }

    // Méthode pour vérifier si le patient est déjà affecté à un lit
    private boolean isPatientAlreadyAssigned(Integer patientKey) {
        BedLocked existingAssignment = bedLockedRepository.findByBedLockedOccupantKy(patientKey);
        return existingAssignment != null;
    }

    private boolean weightCheck(Bed aBed, double patientWeight) {
        if (aBed.getPoids() >= patientWeight) {
            return true;
        } else {
            return false;
        }
    }

    private boolean verifyGoodRoomCondition(Bed bed, double patientWeight) {
        if ((bed.getPhysicalState() == BedPhysicalCondition.Bon_Etat || bed.getPhysicalState() == BedPhysicalCondition.Besoin_De_Reparation_Mineur) && (weightCheck(bed, patientWeight))) {
            return true;
        }
        return false;
    }


    @Override
    public void saveBedLocked(BedLocked iBedLocked) {
        bedLockedRepository.save(iBedLocked);
    }

    @Override
    public BedLocked getBedLockedByKey(Integer iOccupantKey) {
        BedLocked abedLocked = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(iOccupantKey, OccupantType.Patient);
        if (abedLocked != null) {
            return abedLocked;
        } else {
            throw new IllegalStateException("Aucune bedLocked existe avec cette Id");

        }

    }


    public Optional<BedLocked> findById(Integer id) {
        Optional<BedLocked> aBedLockedOptional = bedLockedRepository.findById(id);
        if (aBedLockedOptional.isPresent()) {
            return aBedLockedOptional;
        }
        throw new IllegalStateException("l' Id de bedLocked n'existe pas!");
    }


    @Override
    public boolean patientCheck(Integer id) {
        BedLocked aBedLocked = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(id, OccupantType.Patient);
        if (aBedLocked != null) {
            return true;
        }
        return false;

    }

    @Override
    public void changePatientReakEnteryDate(Integer id, Date bedLocked_RealUnxTmBgn) {
        BedLocked aBedLockedOptional = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(id, OccupantType.Patient);
        if (aBedLockedOptional != null) {
            if(checkAccompagnant(id)){
                Integer accompagnantKey=getAccompagnat(id);
                BedLocked accompagnantBedLocked=bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(accompagnantKey,OccupantType.Accompagnant);
                if(accompagnantBedLocked!=null){
                    aBedLockedOptional.setBedLocked_RealUnxTmBgn(bedLocked_RealUnxTmBgn);
                    accompagnantBedLocked.setBedLocked_RealUnxTmBgn(bedLocked_RealUnxTmBgn);
                    bedLockedRepository.save(aBedLockedOptional);
                }
            }else {

                aBedLockedOptional.setBedLocked_RealUnxTmBgn(bedLocked_RealUnxTmBgn);
                bedLockedRepository.save(aBedLockedOptional);
            }
        } else {
            throw new IllegalStateException("le patient n'existe pas");
        }

    }

    String historySave = "http://localhost:8091/historique";
    RestTemplate restTemplate = new RestTemplate();
    String getPatient = "http://localhost:8091/patients/{patientKey}";

    RestTemplate restTemplate2 = new RestTemplate();

    /* @Override
     public Bed changePatientBed(Integer roomKey, BedLocked newBedLocked, boolean accompagnement, double patientWeight) {
         try {

             bedLockedRepository.save(newBedLocked);

             Optional<Room> optionalRoom = roomRepository.findById(roomKey);
             if (optionalRoom.isPresent()) {
                 Room room = optionalRoom.get();
                 List<Bed> beds = room.getRoomBed();

                 ////////chambre Double////
                 if (room.getRoomType() == RoomType.Double) {
                     if (accompagnement) {
                         long simpleBedCount = 0;
                         long medicalBedCount = 0;
                         for (Bed abed : beds) {
                             if (abed.getBedType() == BedType.Simple) {
                                 simpleBedCount++;
                             } else if (abed.getBedType() == BedType.Medicalise) {
                                 medicalBedCount++;
                             }
                         }

                         if (simpleBedCount == 1 && medicalBedCount == 1) {
                             for (Bed abed : beds) {
                                 if (abed.getBedType() == BedType.Medicalise && newBedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                     assignPersonToBed(newBedLocked, abed);
                                     return abed;
                                 }
                             }
                         } else if (simpleBedCount == 2) {
                             for (Bed abed : beds) {
                                 if (abed.getBedType() == BedType.Simple && newBedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                     assignPersonToBed(newBedLocked, abed);
                                     return abed;
                                 }
                             }
                         } else {
                             throw new IllegalStateException("La chambre double ne contient pas la configuration attendue de lits");
                         }
                     } else if (!accompagnement) {
                         Date patientEntry = newBedLocked.getBedLocked_PlannedUnxTmBgn();
                         Date patientExit = newBedLocked.getBedLocked_PlannedUnxTmEnd();
                         for (Bed abed : beds) {
                             if (isBedInBedLocked(abed)) {
                                 if (checkdates(abed, patientEntry, patientExit) && verifyGoodRoomCondition(abed, patientWeight)) {
                                     assignPersonToBed(newBedLocked, abed);
                                     return abed;
                                 }
                             } else if (!isBedInBedLocked(abed) && verifyGoodRoomCondition(abed, patientWeight)) {
                                 assignPersonToBed(newBedLocked, abed);
                                 return abed;
                             }
                         }
                     }
                 }

                 ////////chambre COLLECTIVE////

                 else if (room.getRoomType() == RoomType.COLLECTIVE) {
                     Date patientEntry = newBedLocked.getBedLocked_PlannedUnxTmBgn();
                     Date patientExit = newBedLocked.getBedLocked_PlannedUnxTmEnd();
                     boolean bedFound = false;
                     for (Bed bed : beds) {
                         if (isBedInBedLocked(bed)) {
                             if (checkdates(bed, patientEntry, patientExit) && verifyGoodRoomCondition(bed, patientWeight)) {
                                 assignPersonToBed(newBedLocked, bed);
                                 bedFound = true;
                                 return bed;
                             }
                         } else if (!isBedInBedLocked(bed) && verifyGoodRoomCondition(bed, patientWeight)) {
                             assignPersonToBed(newBedLocked, bed);
                             bedFound = true;
                             return bed;
                         }
                     }
                     if (!bedFound) {
                         throw new IllegalStateException("Aucun lit disponible avec les bons critères !");
                     }
                 }

                 ////////chambre Simple////

                 else if (room.getRoomType() == RoomType.Simple) {
                     Date patientEntry = newBedLocked.getBedLocked_PlannedUnxTmBgn();
                     Date patientExit = newBedLocked.getBedLocked_PlannedUnxTmEnd();
                     boolean bedFound = false;
                     for (Bed bed : beds) {
                         if (isBedInBedLocked(bed)) {
                             if (checkdates(bed, patientEntry, patientExit) && verifyGoodRoomCondition(bed, patientWeight)) {
                                 assignPersonToBed(newBedLocked, bed);
                                 bedFound = true;
                                 return bed;
                             }
                         } else if (!isBedInBedLocked(bed) && verifyGoodRoomCondition(bed, patientWeight)) {
                             assignPersonToBed(newBedLocked, bed);
                             bedFound = true;
                             return bed;
                         }
                     }
                     if (!bedFound) {
                         throw new IllegalStateException("Aucun lit disponible avec les bons critères !");
                     }
                 }

                 else {
                     if (!beds.isEmpty()) {
                         assignPersonToFirstAvailableBed(newBedLocked, beds);
                     } else {
                         throw new IllegalStateException("Pas de lit disponible dans la chambre " + room.getRoomType());
                     }
                 }






             }










             else {
                 throw new IllegalStateException("Chambre non trouvée avec l'ID: " + roomKey);
             }



         } catch (Exception e) {
             throw new IllegalStateException(e.getMessage());
         }
         return null;
     }
 */
   /* @Override
    public Bed changePatientBed(Integer roomKey, BedLocked newBedLocked, boolean accompagnement, double patientWeight) {

        try {

            bedLockedRepository.save(newBedLocked);

            Optional<Room> optionalRoom = roomRepository.findById(roomKey);
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                List<Bed> beds = room.getRoomBed();

                ////////chambre Double////
                if (room.getRoomType() == RoomType.Double) {
                    if (accompagnement) {
                        long simpleBedCount = 0;
                        long medicalBedCount = 0;
                        for (Bed abed : beds) {
                            if (abed.getBedType() == BedType.Simple) {
                                simpleBedCount++;
                            } else if (abed.getBedType() == BedType.Medicalise) {
                                medicalBedCount++;
                            }
                        }

                        if (simpleBedCount == 1 && medicalBedCount == 1) {
                            for (Bed abed : beds) {
                                if (abed.getBedType() == BedType.Medicalise && newBedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                    assignPersonToBed(newBedLocked, abed);

                                    return abed;

                                }
                            }
                        } else if (simpleBedCount == 2) {
                            for (Bed abed : beds) {
                                if (abed.getBedType() == BedType.Simple && newBedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                    assignPersonToBed(newBedLocked, abed);
                                    return abed;
                                }
                            }
                        } else {
                            throw new IllegalStateException("La chambre double ne contient pas la configuration attendue de lits");
                        }
                    } else if (!accompagnement) {
                        Date patientEntry = newBedLocked.getBedLocked_PlannedUnxTmBgn();
                        Date patientExit = newBedLocked.getBedLocked_PlannedUnxTmEnd();
                        for (Bed abed : beds) {
                            if (isBedInBedLocked(abed)) {
                                if (checkdates(abed, patientEntry, patientExit) && verifyGoodRoomCondition(abed, patientWeight)) {
                                    assignPersonToBed(newBedLocked, abed);
                                    return abed;
                                }
                            } else if (!isBedInBedLocked(abed) && verifyGoodRoomCondition(abed, patientWeight)) {
                                assignPersonToBed(newBedLocked, abed);
                                return abed;
                            }
                        }
                    }
                }

                ////////chambre COLLECTIVE////

                else if (room.getRoomType() == RoomType.COLLECTIVE) {
                    Date patientEntry = newBedLocked.getBedLocked_PlannedUnxTmBgn();
                    Date patientExit = newBedLocked.getBedLocked_PlannedUnxTmEnd();
                    boolean bedFound = false;
                    for (Bed bed : beds) {
                        if (isBedInBedLocked(bed)) {
                            if (checkdates(bed, patientEntry, patientExit) && verifyGoodRoomCondition(bed, patientWeight)) {
                                assignPersonToBed(newBedLocked, bed);
                                bedFound = true;
                                return bed;
                            }
                        } else if (!isBedInBedLocked(bed) && verifyGoodRoomCondition(bed, patientWeight)) {
                            assignPersonToBed(newBedLocked, bed);
                            bedFound = true;
                            return bed;
                        }
                    }
                    if (!bedFound) {
                        throw new IllegalStateException("Aucun lit disponible avec les bons critères !");
                    }
                }

                ////////chambre Simple////

                else if (room.getRoomType() == RoomType.Simple) {
                    Date patientEntry = newBedLocked.getBedLocked_PlannedUnxTmBgn();
                    Date patientExit = newBedLocked.getBedLocked_PlannedUnxTmEnd();
                    boolean bedFound = false;
                    for (Bed bed : beds) {
                        if (isBedInBedLocked(bed)) {
                            if (checkdates(bed, patientEntry, patientExit) && verifyGoodRoomCondition(bed, patientWeight)) {
                                assignPersonToBed(newBedLocked, bed);
                                bedFound = true;
                                return bed;
                            }
                        } else if (!isBedInBedLocked(bed) && verifyGoodRoomCondition(bed, patientWeight)) {
                            assignPersonToBed(newBedLocked, bed);
                            bedFound = true;
                            return bed;
                        }
                    }
                    if (!bedFound) {
                        throw new IllegalStateException("Aucun lit disponible avec les bons critères !");
                    }
                } else {
                    if (!beds.isEmpty()) {
                        assignPersonToFirstAvailableBed(newBedLocked, beds);
                    } else {
                        throw new IllegalStateException("Pas de lit disponible dans la chambre " + room.getRoomType());
                    }
                }


            } else {
                throw new IllegalStateException("Chambre non trouvée avec l'ID: " + roomKey);
            }


        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        return null;
    }*/

@Override
    public Bed changePatientBed(Integer roomKey, BedLocked newBedLocked, boolean accompagnement, double patientWeight,Integer commpanionKey) {

    Bed patientBed=null;
    try {

        bedLockedRepository.save(newBedLocked);
        Date EnterDate = newBedLocked.getBedLocked_PlannedUnxTmBgn();
        Date ExitDate = newBedLocked.getBedLocked_PlannedUnxTmEnd();
        Date realEntery = newBedLocked.getBedLocked_RealUnxTmBgn();
        Date realExit = newBedLocked.getBedLocked_RealUnxTmEnd();

        BedLocked companionBedLocked = new BedLocked();

        Optional<Room> optionalRoom = roomRepository.findById(roomKey);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            List<Bed> beds = room.getRoomBed();
            RoomCompanion roomCompanion;
         //   Integer accompanionKey=null;
            ////////chambre Double////
            if (room.getRoomType() == RoomType.Double) {
                if (accompagnement) {
                    long simpleBedCount = 0;
                    long medicalBedCount = 0;
                    for (Bed abed : beds) {
                        if (abed.getBedType() == BedType.Simple) {
                            simpleBedCount++;
                        } else if (abed.getBedType() == BedType.Medicalise) {
                            medicalBedCount++;
                        }
                    }

                    if (simpleBedCount == 1 && medicalBedCount == 1) {
                        // Vérifie s'il y a un accompagnant



                            for (Bed abed : beds) {
                                if (abed.getBedType() == BedType.Medicalise && newBedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                    // Assigner le patient au lit médicalisé
                                    assignPersonToBed(newBedLocked, abed);
                                    patientBed = abed;
                                } else if (abed.getBedType() == BedType.Simple && verifyGoodRoomCondition(abed, patientWeight)) {

                                    if (commpanionKey != null) {
                                        System.out.println("Companion Key " + commpanionKey);
                                        Optional<RoomCompanion> companion = roomCompanionService.getCompanionById(commpanionKey);

                                        if (companion.isPresent()) {

                                            roomCompanion=companion.get();
                                            System.out.println("L'accompagnant est "+roomCompanion);
                                            // Vérifier les dates et affecter l'accompagnant au lit simple
                                            companionBedLocked.setBedLockedOccupantKy(commpanionKey);
                                            companionBedLocked.setBedLocked_PlannedUnxTmBgn(EnterDate);
                                            companionBedLocked.setBedLocked_PlannedUnxTmEnd(ExitDate);
                                            companionBedLocked.setBedLocked_RealUnxTmBgn(realEntery);
                                            companionBedLocked.setBedLocked_RealUnxTmEnd(realExit);
                                            companionBedLocked.setBed(abed);
                                            companionBedLocked.setBedLockedOccupantType(OccupantType.Accompagnant);
                                            assignPersonToBed(companionBedLocked, abed);
                                            System.out.println("companionnn beddd"+abed);
                                            bedLockedRepository.save(companionBedLocked);
                                            System.out.println(companionBedLocked);


                                        }else {
                                            // S'il n'y a pas d'accompagnant, assigner le patient au lit médicalisé

                                                if (abed.getBedType() == BedType.Medicalise && newBedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                                    assignPersonToBed(newBedLocked, abed);
                                                    patientBed = abed;

                                            }
                                        }

                                    }

                                }
                            }


                    }


                    else if (simpleBedCount == 2) {
                        for (Bed abed : beds) {
                            if (abed.getBedType() == BedType.Simple && newBedLocked.getBedLockedOccupantType() == OccupantType.Patient) {
                                assignPersonToBed(newBedLocked, abed);
                                patientBed = abed;

                            }
                        }
                    } else {
                        throw new IllegalStateException("La chambre double ne contient pas la configuration attendue de lits");
                    }
                } else if (!accompagnement) {
                    Date patientEntry = newBedLocked.getBedLocked_PlannedUnxTmBgn();
                    Date patientExit = newBedLocked.getBedLocked_PlannedUnxTmEnd();
                    for (Bed abed : beds) {
                        if (isBedInBedLocked(abed)) {
                            if (checkdates(abed, patientEntry, patientExit) && verifyGoodRoomCondition(abed, patientWeight)) {
                                assignPersonToBed(newBedLocked, abed);
                                patientBed = abed;

                            }
                        } else if (!isBedInBedLocked(abed) && verifyGoodRoomCondition(abed, patientWeight)) {
                            assignPersonToBed(newBedLocked, abed);
                            patientBed = abed;

                        }
                    }
                }
            }

            ////////chambre COLLECTIVE////

            else if (room.getRoomType() == RoomType.COLLECTIVE) {
                Date patientEntry = newBedLocked.getBedLocked_PlannedUnxTmBgn();
                Date patientExit = newBedLocked.getBedLocked_PlannedUnxTmEnd();
                boolean bedFound = false;
                for (Bed bed : beds) {
                    if (isBedInBedLocked(bed)) {
                        if (checkdates(bed, patientEntry, patientExit) && verifyGoodRoomCondition(bed, patientWeight)) {
                            assignPersonToBed(newBedLocked, bed);
                            bedFound = true;
                            return bed;
                        }
                    } else if (!isBedInBedLocked(bed) && verifyGoodRoomCondition(bed, patientWeight)) {
                        assignPersonToBed(newBedLocked, bed);
                        bedFound = true;
                        return bed;
                    }
                }
                if (!bedFound) {
                    throw new IllegalStateException("Aucun lit disponible avec les bons critères !");
                }
            }

            ////////chambre Simple////

            else if (room.getRoomType() == RoomType.Simple) {
                Date patientEntry = newBedLocked.getBedLocked_PlannedUnxTmBgn();
                Date patientExit = newBedLocked.getBedLocked_PlannedUnxTmEnd();
                boolean bedFound = false;
                for (Bed bed : beds) {
                    if (isBedInBedLocked(bed)) {
                        if (checkdates(bed, patientEntry, patientExit) && verifyGoodRoomCondition(bed, patientWeight)) {
                            assignPersonToBed(newBedLocked, bed);
                            bedFound = true;
                            patientBed = bed;

                        }
                    } else if (!isBedInBedLocked(bed) && verifyGoodRoomCondition(bed, patientWeight)) {
                        assignPersonToBed(newBedLocked, bed);
                        bedFound = true;
                        patientBed = bed;

                    }
                }
                if (!bedFound) {
                    throw new IllegalStateException("Aucun lit disponible avec les bons critères !");
                }
            } else {
                if (!beds.isEmpty()) {
                    assignPersonToFirstAvailableBed(newBedLocked, beds);
                } else {
                    throw new IllegalStateException("Pas de lit disponible dans la chambre " + room.getRoomType());
                }
            }


        } else {
            throw new IllegalStateException("Chambre non trouvée avec l'ID: " + roomKey);
        }


    } catch (Exception e) {
        throw new IllegalStateException(e.getMessage());
    }
    return patientBed;
}









    @Override

    public Room getPatientRoom(Integer patientKey) {
        BedLocked bedLocked = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(patientKey, OccupantType.Patient);
        if (bedLocked != null) {
            Bed bed = bedLocked.getBed();
            if (bed != null) {
                return bed.getRoomBed();
            } else {
                // Gérer le cas où le lit n'est pas trouvé
                throw new RuntimeException("Le lit du patient n'a pas été trouvé.");
            }
        } else {
            // Gérer le cas où le patient n'est pas trouvé dans un lit
            throw new RuntimeException("Le patient n'est pas logé dans un lit.");
        }
    }


    @Override
    public BedLocked getBedLockedcompanion(Integer comapnionKey) {
        return bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(comapnionKey, OccupantType.Accompagnant);
    }

    @Override
    public Bed getBedbyType(BedType type) {
        return (Bed) bedRespository.findByBedType(type);
    }

    @Override
    public boolean checkAccompagnant(Integer patientKey) {
        BedLocked abedLocked = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(patientKey, OccupantType.Patient);
        Bed aBed = abedLocked.getBed();
        Room aRoom = aBed.getRoomBed();

        if (aRoom.getRoomType() == RoomType.Double) {
            for (Bed beed : aRoom.getRoomBed()) {
                if (beed.getBedType() == BedType.Simple) {
                    Integer bedKey = beed.getBedKey();
                    BedLocked abedlockedcompanion = bedLockedRepository.findByBed_BedKey(bedKey);
                    if (abedlockedcompanion != null) {
                        if (abedlockedcompanion.getBedLockedOccupantType() == OccupantType.Accompagnant) {
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }


    @Override
    public Integer getAccompagnat(Integer patientKey) {
        BedLocked abedLocked = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(patientKey, OccupantType.Patient);
        Bed aBed = abedLocked.getBed();
        Room aRoom = aBed.getRoomBed();

        if (aRoom.getRoomType() == RoomType.Double) {
            for (Bed beed : aRoom.getRoomBed()) {
                if (beed.getBedType() == BedType.Simple) {
                    Integer bedKey = beed.getBedKey();
                    BedLocked abedlockedcompanion = bedLockedRepository.findByBed_BedKey(bedKey);
                    if (abedlockedcompanion != null) {
                        if (abedlockedcompanion.getBedLockedOccupantType() == OccupantType.Accompagnant) {
                            return abedlockedcompanion.getBedLockedOccupantKy();
                        }
                    }
                }

            }
        }
       return null;
    }



    @Override

    public  void libererPatientFromBed(Integer patientKey) {
        BedLocked abedLocked = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(patientKey, OccupantType.Patient);
        if (abedLocked != null) {
            Bed patientBed = abedLocked.getBed();
            patientBed.setBedStatue(BedStatus.Disponible);
            patientBed.setBedCleaningStatus(BedCleaningStatus.A_Nettoyer);

            // Check if patient has an accompanying person
            if (checkAccompagnant(patientKey)) {
                Integer accompagnantKey = getAccompagnat(patientKey);
                BedLocked accompagnantBedLocked = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(accompagnantKey, OccupantType.Accompagnant);
                if (accompagnantBedLocked != null) {
                    Bed accompagnantBed = accompagnantBedLocked.getBed();
                    accompagnantBed.setBedStatue(BedStatus.Disponible);
                    accompagnantBed.setBedCleaningStatus(BedCleaningStatus.A_Nettoyer);
                    bedLockedRepository.delete(accompagnantBedLocked);
                }
            }
            // Delete the patient's bed lock entry
            bedLockedRepository.delete(abedLocked);
        } else {
            throw new IllegalStateException("Le patient n'est pas associé à un lit");
        }
    }




@Override
    public  void  addRealExitDate(Integer id, Date bedLocked_RealUnxTmEnd){
    BedLocked aBedLockedOptional = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(id, OccupantType.Patient);
    if (aBedLockedOptional != null) {
        if(checkAccompagnant(id)){
            Integer accompagnantKey=getAccompagnat(id);
            BedLocked accompagnantBedLocked=bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(accompagnantKey,OccupantType.Accompagnant);
            if(accompagnantBedLocked!=null){
                aBedLockedOptional.setBedLocked_RealUnxTmBgn(bedLocked_RealUnxTmEnd);
                accompagnantBedLocked.setBedLocked_RealUnxTmBgn(bedLocked_RealUnxTmEnd);
                bedLockedRepository.save(aBedLockedOptional);
            }
        }else {

            aBedLockedOptional.setBedLocked_RealUnxTmBgn(bedLocked_RealUnxTmEnd);
            bedLockedRepository.save(aBedLockedOptional);
        }
    } else {
        throw new IllegalStateException("le patient n'existe pas");
    }

}
/*
@Override
public    void prolongation(Integer id, Date bedLocked_PlannedUnxTmEnd){
 Date PlannedEnterDate;
 Date PlannedExitDate;
    BedLocked aBedLockedOptional = bedLockedRepository.findByBedLockedOccupantKyAndAndBedLockedOccupantType(id, OccupantType.Patient);
    if (aBedLockedOptional != null) {
        Bed bed=aBedLockedOptional.getBed();
List<BedLocked >bedLockedList=bed.getBedLockeds();
for(BedLocked bedLocked :bedLockedList){
  PlannedEnterDate=bedLocked.getBedLocked_PlannedUnxTmBgn();
  PlannedExitDate=bedLocked.getBedLocked_PlannedUnxTmEnd();
  if(checkdates())

}


    }
    }
*/




}







