package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.*;
import com.core.Parameterization.Respositories.*;
import com.core.Parameterization.Services.CareUnitService;
import com.core.Parameterization.Services.EquipmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
 public class CareUnitServiceImpl implements CareUnitService {
    @Autowired
    private CareUnitRepository careUnitRepository;
    @Autowired
    private EquipementRepository equipementRepository;
    @Autowired

    private CareUnitEquipLinkRepo careUnitEquipLink;

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BedLockedRepository bedLockedRepository;

    @Autowired
    private BedRespository bedRespository;

    public CareUnitServiceImpl(CareUnitRepository careUnitRepository) {
        this.careUnitRepository = careUnitRepository;
    }


    @Override
    public CareUnit create(CareUnit iCareUnit) {
        CareUnit aCareUnitName = careUnitRepository.findByCareunitName(iCareUnit.getCareunitName());
        if (aCareUnitName != null) {
            throw new IllegalStateException("Le nom de cette unité déja existe , donner un nom unique !");


        } else {
            careUnitRepository.save(iCareUnit);
        }
        return iCareUnit;
    }

    @Override
    public List<CareUnit> retreivesAllCareUnits() {
        return careUnitRepository.findAll();

    }

    @Override
    public Optional<CareUnit> getCareUnitByKey(Integer iCareunitKey) {
        return careUnitRepository.findById(iCareunitKey);
    }

    @Override
    public CareUnit getCareUnitbyNme(String iCareunitName) {
        return careUnitRepository.findByCareunitName(iCareunitName);
    }

    public List<CareUnit> getCareUnitByStatue(UnitStatus iCareunitStatue) {
        return careUnitRepository.findBycareunitStatue(iCareunitStatue);

    }

    @Override
    public void update(Integer iCareunitKey, CareUnit iNewCareUnit) {
        CareUnit aExistingCareUnit = careUnitRepository.findById(iCareunitKey).orElse(null);
        if (aExistingCareUnit != null) {
            CareUnit aCareunitName = careUnitRepository.findByCareunitName(iNewCareUnit.getCareunitName());
            if ((aCareunitName != null) && (aCareunitName.getCareunitKey() != aExistingCareUnit.getCareunitKey())) {
                throw new IllegalStateException("Le nom de cette unité déja existe , donner un nom unique !");


            } else {
                aExistingCareUnit.setCareunitName(iNewCareUnit.getCareunitName());
                aExistingCareUnit.setCareunitCapacity(iNewCareUnit.getCareunitCapacity());
                aExistingCareUnit.setCareunitDescription(iNewCareUnit.getCareunitDescription());
                aExistingCareUnit.setCareunitResponsable(iNewCareUnit.getCareunitResponsable());
                aExistingCareUnit.setCareUnit_StartTime(iNewCareUnit.getCareUnit_StartTime());
                aExistingCareUnit.setCareUnit_EndTime(iNewCareUnit.getCareUnit_EndTime());
                aExistingCareUnit.setOperationhoure(iNewCareUnit.getOperationhoure());
                aExistingCareUnit.setCareunitStatue(iNewCareUnit.getCareunitStatue());
                aExistingCareUnit.setCareuniType(iNewCareUnit.getCareuniType());


                careUnitRepository.save(aExistingCareUnit);
            }
        } else {
            throw new IllegalStateException("Aucune unité existe avec cet ID !");
        }
    }

    @Override
    public void delete(Integer iCareunitKey) {
        CareUnit aNewCareUnit = careUnitRepository.findById(iCareunitKey).orElse(null);
        if (aNewCareUnit != null) {
            careUnitRepository.deleteById(iCareunitKey);
        } else {
            throw new IllegalStateException("Aucune unité existe avec cet ID !");
        }

    }

    @Override
    public List<Room> getRoom(CareUnit iCareUnit) {
        return iCareUnit.getRooms();

    }

    @Override
    public void addRoom(CareUnit iCareUnit, Room iNewRoom) {
        // Vérification de la capacité en fonction du type de chambre
        int aRoomCapacity = iNewRoom.getRoomCapacity();
        if (iNewRoom.getRoomType() == RoomType.Simple && aRoomCapacity != 1) {
            throw new IllegalStateException("La capacité d'une chambre simple doit être de 1");
        }
        if (iNewRoom.getRoomType() == RoomType.Double && (aRoomCapacity != 1 && aRoomCapacity != 2)) {
            throw new IllegalStateException("La capacité d'une chambre double doit être de 1 ou 2");
        }

        // Vérification de la capacité de l'unité de soins
        int aCareUnitCapacity = iCareUnit.getCareunitCapacity();
        List<Room> aRooms = iCareUnit.getRooms();
        if (aRooms.size() >= aCareUnitCapacity) {
            throw new IllegalStateException("L'unité de soins est pleine");
        }

        // Vérification si le nom de la chambre existe déjà dans l'unité de soins
        boolean aRoomNameExists = aRooms.stream().anyMatch(room -> room.getRoomName().equals(iNewRoom.getRoomName()));
        if (aRoomNameExists) {
            throw new IllegalStateException("Le nom de chambre existe déjà dans cette unité, veuillez donner un nom unique !");
        }

        // Ajout de la nouvelle chambre à l'unité de soins
        aRooms.add(iNewRoom);
        iNewRoom.setCareunitRoom(iCareUnit);
        careUnitRepository.save(iCareUnit);
    }


    @Override
    public void updateCareUnit(CareUnit iCareUnit) {
        careUnitRepository.save(iCareUnit);
    }


    @Override
    public Set<CareUnitEquipLink> getEquipmentsByCareUnit(Integer iCareUnitId) {
        CareUnit aCareUnit = careUnitRepository.findById(iCareUnitId).orElseThrow(() -> new RuntimeException("L'unite de soin n'existe pas "));
        return aCareUnit.getCareUnitEquipLinkSet();
    }


    @Transactional
    public void removeEquipmentFromCareUnit(Integer iCareUnitId, Long iEquipmentId) {
        // Retrieve all associations between the care unit and equipment
        List<CareUnitEquipLink> aAssociations = careUnitEquipLink.findByCareUnit_CareunitKeyAndEquipment_Equipementkey(iCareUnitId, iEquipmentId);

        if (!aAssociations.isEmpty()) {
            // Sélectionnez la première association trouvée et supprimez-la
            CareUnitEquipLink aAssociationToDelete = aAssociations.get(0);
            careUnitEquipLink.delete(aAssociationToDelete);
        } else {
            // If no associations are found, throw an exception
            throw new IllegalArgumentException("Aucune association existe entre l'equipement et cette unité ");
        }
    }


    public void addEquipmentToCareUnit(CareUnitEquipLink iAssociation) {
        careUnitEquipLink.save(iAssociation);
    }

    @Override
    @Transactional
    public void updateEquipmentInCareUnit(Integer iCareUnitId, List<Long> ioldEquipmentList, List<Long> iNewEquipmentList) {
        CareUnit aCareUnit = careUnitRepository.findById(iCareUnitId)
                .orElseThrow(() -> new IllegalArgumentException("Aucune unité de soin existe avec cette ID  !"));

        for (int i = 0; i < ioldEquipmentList.size(); i++) {
            Long aOldEquipmentId = ioldEquipmentList.get(i);
            Long aNewEquipmentId = iNewEquipmentList.get(i);

            // Check if the old equipment ID exists in the CareUnitEquipLink association
            CareUnitEquipLink equipLinkToUpdate = aCareUnit.getCareUnitEquipLinkSet().stream()
                    .filter(link -> link.getEquipment().getEquipementkey().equals(aOldEquipmentId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("L' equipement n'existe pas dans cette unité  "));

            // Check if the new equipment ID exists in the equipment table
            Equipment newEquipment = equipementRepository.findById(aNewEquipmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Aucun equipement existe avec cette ID"));

            // Update the equipment ID in the association
            equipLinkToUpdate.setEquipment(newEquipment);
        }

        // Save the updated CareUnit entity
        careUnitRepository.save(aCareUnit);
    }


    @Override
    public List<CareUnit> searchByCriteria(int careunitCapacity, UnitType careuniType, UnitStatus careunitStatue) {
        List<CareUnit> listCareUnit = careUnitRepository.findByCareunitCapacityAndCareuniTypeAndCareunitStatue(careunitCapacity, careuniType, careunitStatue);
        if (listCareUnit.size() > 0) {
            return listCareUnit;
        } else {
            throw new IllegalArgumentException("Il n'xiste aucune unité avec ces critéres ! ");
        }
    }

    /* @Override
     public List<Room> getRoomsInCareUnit(Integer careunitKey, RoomType roomType) {
         List<Room> aRoomList = roomRepository.findByCareunitRoom_CareunitKeyAndRoomType(careunitKey, roomType);
         List<Room> filteredRooms = new ArrayList<>();
         Date PatientEnteryPlanned;
         if (aRoomList.isEmpty()) {
             throw new IllegalStateException("Aucune chambre dans cette unité");
         }

         for (Room aRoom : aRoomList) {
             boolean isGoodOrNeedsMinorRepair = false;

             for (Bed aBed : aRoom.getRoomBed()) {
                 // Check if the bed is in good condition or needs minor repairs
                 if ((aBed.getPhysicalState() == BedPhysicalCondition.Bon_Etat ||
                         aBed.getPhysicalState() == BedPhysicalCondition.Besoin_De_Reparation_Mineur) &&
                         !isBedInBedLocked(aBed)) {
                     isGoodOrNeedsMinorRepair = true;
                     break; // Exit loop as soon as we find a bed in good condition or needing minor repairs

                 }
             }

             if (isGoodOrNeedsMinorRepair) {
                 filteredRooms.add(aRoom); // If at least one bed in the room is in good condition or needs minor repairs, add the room to the filtered list
             }
         }

         return filteredRooms;
     }
 */
    @Override
    @Transactional

    /*public List<Room> getRoomsInCareUnit(Integer careunitKey, RoomType roomType, Date bedLocked_PlannedUnxTmBgn, Date bedLocked_PlannedUnxTmEnd, double patientWeight, boolean accompagnement) {
        List<Room> aRoomList = roomRepository.findByCareunitRoom_CareunitKeyAndRoomType(careunitKey, roomType);
       //declaration
        List<Room> filteredRooms = new ArrayList<>();

        if (aRoomList.isEmpty()) {
            throw new IllegalStateException("Aucune chambre de type " + roomType + " dans cette unité");
        }

        for (Room aRoom : aRoomList) {
            int simpleBedCount = 0;
            int medicalBedCount = 0;
            boolean isGoodOrNeedsMinorRepair = false;
            boolean isNotReserved = false;
            boolean isPerfectWeight = false;

            for (Bed aBed : aRoom.getRoomBed()) {
                if (!(isBedInBedLocked(aBed))) {
                    if ((aBed.getBedType() == BedType.Simple || aBed.getBedType() == BedType.Medicalise) && (verifyGoodRoomCondition(aBed,patientWeight))) {
                        if (aBed.getBedType() == BedType.Simple) {
                            simpleBedCount++;
                        } else {
                            medicalBedCount++;
                        }
                        isGoodOrNeedsMinorRepair = true;
                        isNotReserved = true;
                        isPerfectWeight = true;

                    }
                } else if (checkdates(aBed, bedLocked_PlannedUnxTmBgn, bedLocked_PlannedUnxTmEnd)) {
                    if ((aBed.getBedType() == BedType.Simple || aBed.getBedType() == BedType.Medicalise) && verifyGoodRoomCondition(aBed,patientWeight)) {
                        if (aBed.getBedType() == BedType.Simple) {
                            simpleBedCount++;
                        } else {
                            medicalBedCount++;
                        }
                        isGoodOrNeedsMinorRepair = true;
                        isNotReserved = true;
                        isPerfectWeight = true;

                    }
                }
                System.out.println(checkdates(aBed, bedLocked_PlannedUnxTmBgn, bedLocked_PlannedUnxTmEnd));
            }

            if (roomType == RoomType.Double) {

                if ((simpleBedCount == 2 || (simpleBedCount == 1 && medicalBedCount == 1)) && isGoodOrNeedsMinorRepair && isPerfectWeight && isNotReserved && accompagnement) {
                    filteredRooms.add(aRoom);

                } else if    ((simpleBedCount == 2 || (simpleBedCount == 1 && medicalBedCount == 1) || medicalBedCount == 2) & !accompagnement){
                    for(Bed aBed : aRoom.getRoomBed()){
                        if(verifyGoodRoomCondition(aBed,patientWeight) && isNotReserved ){
                            filteredRooms.add(aRoom);
                        }
                    }


                }
            }


            else if (roomType == RoomType.COLLECTIVE) {
                for (Bed aBed : aRoom.getRoomBed()) {
                    if (verifyGoodRoomCondition(aBed, patientWeight)) {
                        if (isBedInBedLocked(aBed)) {
                            if (checkdates(aBed, bedLocked_PlannedUnxTmBgn, bedLocked_PlannedUnxTmEnd)) {
                                // Si le lit est réservé et que les dates ne se chevauchent pas, il remplit les conditions
                                isNotReserved = true;
                                isGoodOrNeedsMinorRepair = true;
                                isPerfectWeight = true;
                            }
                        } else {
                            // Si le lit n'est pas réservé, il remplit les conditions
                            isNotReserved = true;
                            isGoodOrNeedsMinorRepair = true;
                            isPerfectWeight = true;
                        }
                    }
                }

                // Si au moins un lit remplit toutes les conditions, ajouter la chambre à la liste filtrée
                if (isPerfectWeight && isNotReserved && isGoodOrNeedsMinorRepair) {
                    filteredRooms.add(aRoom);
                } else {
                    throw new IllegalStateException("Aucun lit dans la chambre collective ne remplit les conditions requises.");
                }
            }



            else {
                if (isGoodOrNeedsMinorRepair && isPerfectWeight && !accompagnement && isNotReserved ) {
                    filteredRooms.add(aRoom);
                }
            }
        }

        return filteredRooms;
    }
*/
    public List<Room> getRoomsInCareUnit(Integer careunitKey, RoomType roomType, Date bedLocked_PlannedUnxTmBgn, Date bedLocked_PlannedUnxTmEnd, double patientWeight, boolean accompagnement) {
        List<Room> aRoomList = roomRepository.findByCareunitRoom_CareunitKeyAndRoomType(careunitKey, roomType);
        //declaration
        List<Room> filteredRooms = new ArrayList<>();

        if (aRoomList.isEmpty()) {
            throw new IllegalStateException("Aucune chambre de type " + roomType + " dans cette unité");
        }

        for (Room aRoom : aRoomList) {
            int simpleBedCount = 0;
            int medicalBedCount = 0;
            boolean isGoodOrNeedsMinorRepair = false;
            boolean isNotReserved = false;
            boolean isPerfectWeight = false;

            for (Bed aBed : aRoom.getRoomBed()) {
                if (!(isBedInBedLocked(aBed))) {
                    if ((aBed.getBedType() == BedType.Simple || aBed.getBedType() == BedType.Medicalise) && (verifyGoodRoomCondition(aBed,patientWeight))) {
                        if (aBed.getBedType() == BedType.Simple) {
                            simpleBedCount++;
                        } else {
                            medicalBedCount++;
                        }
                        isGoodOrNeedsMinorRepair = true;
                        isNotReserved = true;
                        isPerfectWeight = true;

                    }
                } else if (checkdates(aBed, bedLocked_PlannedUnxTmBgn, bedLocked_PlannedUnxTmEnd)) {
                    if ((aBed.getBedType() == BedType.Simple || aBed.getBedType() == BedType.Medicalise) && verifyGoodRoomCondition(aBed,patientWeight)) {
                        if (aBed.getBedType() == BedType.Simple) {
                            simpleBedCount++;
                        } else {
                            medicalBedCount++;
                        }
                        isGoodOrNeedsMinorRepair = true;
                        isNotReserved = true;
                        isPerfectWeight = true;

                    }
                }
                System.out.println("reservation de litt :"+checkdates(aBed, bedLocked_PlannedUnxTmBgn, bedLocked_PlannedUnxTmEnd));
            }

            if (roomType == RoomType.Double) {
                if ((simpleBedCount == 2 || (simpleBedCount == 1 && medicalBedCount == 1)) && isGoodOrNeedsMinorRepair && isPerfectWeight && isNotReserved && (accompagnement) ) {
                    filteredRooms.add(aRoom);
                } else if (!accompagnement ) {

                    boolean isValidRoom = false;
                    for (Bed aBed : aRoom.getRoomBed()) {
                        if (verifyGoodRoomCondition(aBed, patientWeight) && isNotReserved) {
                            isValidRoom = true;
                            break; // Sortir de la boucle dès qu'un lit valide est trouvé
                        }
                    }
                    if (isValidRoom) {
                        filteredRooms.add(aRoom);
                    }
                }
            }



            else if (roomType == RoomType.COLLECTIVE) {
                for (Bed aBed : aRoom.getRoomBed()) {
                    if (verifyGoodRoomCondition(aBed, patientWeight)) {
                        if (isBedInBedLocked(aBed)) {
                            if (checkdates(aBed, bedLocked_PlannedUnxTmBgn, bedLocked_PlannedUnxTmEnd)) {
                                // Si le lit est réservé et que les dates ne se chevauchent pas, il remplit les conditions
                                isNotReserved = true;
                                isGoodOrNeedsMinorRepair = true;
                                isPerfectWeight = true;
                            }
                        } else {
                            // Si le lit n'est pas réservé, il remplit les conditions
                            isNotReserved = true;
                            isGoodOrNeedsMinorRepair = true;
                            isPerfectWeight = true;
                        }
                    }
                }

                // Si au moins un lit remplit toutes les conditions, ajouter la chambre à la liste filtrée
                if (isPerfectWeight && isNotReserved && isGoodOrNeedsMinorRepair) {
                    filteredRooms.add(aRoom);
                } else {
                    throw new IllegalStateException("Aucun lit dans la chambre collective ne remplit les conditions requises.");
                }
            }



            else if(roomType == RoomType.Simple && !accompagnement ){
                if(   isGoodOrNeedsMinorRepair &&  isNotReserved &&     isPerfectWeight  ) {
                    filteredRooms.add(aRoom);
                }
                }



            }


        return filteredRooms;
    }

    private boolean isBedInBedLocked(Bed aBed) {
       List<BedLocked> aBedBedLockeds=aBed.getBedLockeds();
       if(!aBedBedLockeds.isEmpty()){
           return true ;
       }
       return false ;


    }

   /* private boolean checkdates(Bed aBed, Date newPatientEntryDate, Date newPatientExitDate) {
        // Vérifier si le lit est réservé dans la table bedLocked
        if (isBedInBedLocked(aBed)) {
            for (BedLocked abedLockeds : aBed.getBedLockeds()) {
                Date plannedUnxTmBgn = abedLockeds.getBedLocked_PlannedUnxTmBgn();
                Date plannedUnxTmEnd = abedLockeds.getBedLocked_PlannedUnxTmEnd();
                Date realUnxBgn = abedLockeds.getBedLocked_RealUnxTmBgn();
                Date realUnxEnd = abedLockeds.getBedLocked_RealUnxTmEnd();

                if ((plannedUnxTmEnd != null && plannedUnxTmBgn != null) || (realUnxBgn != null && realUnxEnd != null)) {
                    // Vérifier si les dates du nouveau patient chevauchent les dates de réservation du lit
                    if ((newPatientEntryDate.after(plannedUnxTmEnd) && newPatientExitDate.after(newPatientEntryDate)) ||
                            (newPatientExitDate.before(plannedUnxTmBgn) && newPatientExitDate.after(newPatientEntryDate))) {
                        return true;
                    } else {
                        if ((newPatientEntryDate.after(realUnxEnd) && (newPatientEntryDate.before(newPatientExitDate))) || (newPatientExitDate.after(realUnxBgn) && (newPatientEntryDate.before(newPatientExitDate)))) {
                            return true;
                        }
                    }
                }
            }
            // Toutes les entrées "bedLocked" ont été vérifiées et aucune n'a de conflit de dates
            return false;
        }
        // Aucune réservation pour ce lit
        return false;
    }*/




    private boolean checkdates(Bed aBed, Date newPatientEntryDate, Date newPatientExitDate) {
        // Vérifier si le lit est réservé dans la table bedLocked
        if (isBedInBedLocked(aBed)) {
            for (BedLocked abedLockeds : aBed.getBedLockeds()) {
                Date plannedUnxTmBgn = abedLockeds.getBedLocked_PlannedUnxTmBgn();
                Date plannedUnxTmEnd = abedLockeds.getBedLocked_PlannedUnxTmEnd();
                Date realUnxBgn = abedLockeds.getBedLocked_RealUnxTmBgn();
                Date realUnxEnd = abedLockeds.getBedLocked_RealUnxTmEnd();

                // Vérifier si les dates du nouveau patient chevauchent les dates de réservation du lit
                if ((plannedUnxTmEnd != null && plannedUnxTmBgn != null)) {
                    if(newPatientExitDate.after(plannedUnxTmBgn) ||(newPatientEntryDate.before(plannedUnxTmEnd))
                            && newPatientExitDate.after(newPatientEntryDate)){
                        return false;
                    }

                }else if(realUnxBgn!=null && realUnxEnd!=null) {
                    if( (newPatientEntryDate.before(realUnxEnd) || (newPatientExitDate.after(realUnxBgn)
                             && newPatientExitDate.after(newPatientEntryDate))))
                    {
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


    private boolean weightCheck(Bed aBed, double patientWeight) {
        if (aBed.getPoids() >= patientWeight) {
            return true;
        } else {
            return false;
        }
    }



private boolean verifyGoodRoomCondition(Bed bed , double patientWeight){
        if((bed.getPhysicalState() == BedPhysicalCondition.Bon_Etat || bed.getPhysicalState() == BedPhysicalCondition.Besoin_De_Reparation_Mineur)&&(weightCheck(bed, patientWeight))){
            return true;
        }
        return false;
}






}





















