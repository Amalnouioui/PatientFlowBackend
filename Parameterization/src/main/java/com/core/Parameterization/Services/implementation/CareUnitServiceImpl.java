package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.RoomType;
import com.core.Parameterization.Entities.Enumeration.UnitStatus;
import com.core.Parameterization.Entities.Enumeration.UnitType;
import com.core.Parameterization.Respositories.*;
import com.core.Parameterization.Services.CareUnitEquipLinkService;
import com.core.Parameterization.Services.CareUnitService;
import com.core.Parameterization.Services.EquipmentService;
import com.core.Parameterization.Services.ServiceSer;
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
    private CareUnitServiceLinkRepo careUnitServiceLinkRepo;
    @Autowired
    private ServiceRepo serviceRepo;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ServiceSer serviceSer;


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


    @Transactional
    public void addServices(CareUnitServiceLink iCareUnitServiceLink){

        careUnitServiceLinkRepo.save(iCareUnitServiceLink);

    }
@Override
@Transactional
    public void updateService(Integer iCareUnitId,  List<Long>iOldServiceList, List<Long> iNewServiceList) {

    CareUnit careUnit = careUnitRepository.findById(iCareUnitId)
            .orElseThrow(() -> new IllegalArgumentException("Aucune unit existe avec cette ID"));

    for (int i = 0; i < iOldServiceList.size(); i++) {
        Long aOldServiceId = iOldServiceList.get(i);
        Long aNewServiceId = iNewServiceList.get(i);

        // Check if the old equipment ID exists in the CareUnitEquipLink association
        CareUnitServiceLink aCareUnitServiceLink = careUnit.getCareUnitServiceLinkSet().stream()
                .filter(link -> link.getService().getServiceKy().equals(aOldServiceId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Le service n'existe pas dans cette unité de soin "));

        // Check if the new equipment ID exists in the equipment table
        ServiceEntity aServiceEntity = serviceRepo.findById(aNewServiceId)
                .orElseThrow(() -> new IllegalArgumentException("Aucun service avec cette ID"));

        // Update the equipment ID in the association
        aCareUnitServiceLink.setService(aServiceEntity);
    }


}


@Override
    @Transactional
    public void removeService(Integer iCareUnitId, Long iServiceKy) {
        // Vérifier si l'association entre l'unité de soins et l'équipement existe
        List<CareUnitServiceLink> aAssociations = careUnitServiceLinkRepo.findByService_ServiceKyAndCareUnit_CareunitKey(iServiceKy, iCareUnitId);
        if (!aAssociations.isEmpty()) {
            CareUnitServiceLink aAssociationToDelete = aAssociations.get(0);
            // Supprimer l'association
            careUnitServiceLinkRepo.delete(aAssociationToDelete);
        } else {
            // Lancer une exception si l'association n'existe pas
            throw new IllegalArgumentException("Aucune association existe entre le service et cette unité ");
        }
    }
    @Override
    public Set<CareUnitServiceLink> getServicesByCareUnit(Integer iCareUnitId) {
        CareUnit aCareUnit = careUnitRepository.findById(iCareUnitId).orElseThrow(() -> new RuntimeException("Aucune  unité existe avec cette ID"));
        return aCareUnit.getCareUnitServiceLinkSet();
    }


   @Override
    public List<CareUnit> searchByCriteria(int careunitCapacity, UnitType careuniType, UnitStatus careunitStatue) {
        List<CareUnit> listCareUnit=careUnitRepository.findByCareunitCapacityAndCareuniTypeAndCareunitStatue(careunitCapacity,careuniType,careunitStatue);
        if(listCareUnit.size()>0) {
            return listCareUnit;
        }
        else {
            throw new IllegalArgumentException("Il n'xiste aucune unité avec ces critéres ! ");
        }
    }


}































