package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.BedPhysicalCondition;
import com.core.Parameterization.Entities.Enumeration.BedStatus;
import com.core.Parameterization.Entities.Enumeration.BedType;
import com.core.Parameterization.Entities.Enumeration.RoomType;
import com.core.Parameterization.Respositories.BedEquiLinkRepo;
import com.core.Parameterization.Respositories.BedRespository;
import com.core.Parameterization.Respositories.EquipementRepository;
import com.core.Parameterization.Respositories.RoomRepository;
import com.core.Parameterization.Services.BedService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BedServiceImpl implements BedService {
    @Autowired
    private RoomRepository roomRepository;
    private BedRespository bedRespository;
    private EquipementRepository equipementRepository;
    private BedEquiLinkRepo bedEquiLinkRepo;

    @Autowired
    public BedServiceImpl(BedRespository iBedRepository, RoomRepository iRoomRepository, EquipementRepository iEquipementRepository, BedEquiLinkRepo iBedEquiLinkRepo) {

        this.bedRespository = iBedRepository;
        this.roomRepository = iRoomRepository;
        this.equipementRepository = iEquipementRepository;
        this.bedEquiLinkRepo = iBedEquiLinkRepo;

    }

    @Override
    public void create(Bed iBed) {
        Optional<Room> aRoomOptional = roomRepository.findById(iBed.getRoomBed().getRoomKey());
        if (aRoomOptional.isPresent()) {
            Room aRoom = aRoomOptional.get();
            List<Bed> aBedList = aRoom.getRoomBed();
            int aRoomCapacity = aRoom.getRoomCapacity();




            if (aRoom.getRoomType() == RoomType.COLLECTIVE && aRoomCapacity < 3 && iBed.getBedType() == BedType.Simple && iBed.getBedType() == BedType.Medicalise) {
                throw new IllegalStateException("Impossible d'ajouter un lit simple dans une chambre collective avec une capacité inférieure à 3");
            }

            if (aBedList.size() >= aRoomCapacity) {
                throw new IllegalStateException("La chambre "+aRoom.getRoomName()+" est pleine !");
            }
            // Vérifier si le numéro de lit existe déjà dans la chambre
            boolean aBedNumberExistsInRoom = aBedList.stream()
                    .anyMatch(existingBed -> existingBed.getBedNumber().equals(iBed.getBedNumber()));
            if (aBedNumberExistsInRoom) {
                throw new IllegalStateException("Le numéro de lit existe déjà dans la chambre, veuillez fournir un numéro unique !");
            }

            // Enregistrement du lit si toutes les vérifications sont réussies
            bedRespository.save(iBed);
        } else {
            throw new IllegalStateException("La chambre spécifiée n'existe pas !");
        }
    }



    @Override
    public List<Bed> retrieveBeds() {
        return bedRespository.findAll();
    }

    @Override
    public Optional<Bed> getBedByKey(Integer iBedKy) {
        return bedRespository.findById(iBedKy);
    }

    @Override
    public void update(Integer iBedKey, Bed iUpdatedBed) {
        Bed aExistingBed = bedRespository.findById(iBedKey).orElseThrow(() -> new EntityNotFoundException("Aucun lit existe avec cet ID"));
        Room aRoomBed =aExistingBed.getRoomBed() ;
        int aRoomCapacity = aRoomBed.getRoomCapacity();


        // Check if the new bed number is already used in the room
        Bed aBedWithSameNumberInRoom = bedRespository.findByBedNumberAndRoomBed(iUpdatedBed.getBedNumber(), aExistingBed.getRoomBed());
        if (aBedWithSameNumberInRoom != null && !aBedWithSameNumberInRoom.getBedKey().equals(aExistingBed.getBedKey())) {
            throw new IllegalStateException("Le nom de cet lit  déja existe , donner un nom unique !");
        }
        // Update the bed details
        aExistingBed.setBedNumber(iUpdatedBed.getBedNumber());
        aExistingBed.setBedType(iUpdatedBed.getBedType());
        aExistingBed.setBedDescription(iUpdatedBed.getBedDescription());
        aExistingBed.setBedStatue(iUpdatedBed.getBedStatue());
        aExistingBed.setBedPurchaseDate(iUpdatedBed.getBedPurchaseDate());
        aExistingBed.setBedCleaningStatus(iUpdatedBed.getBedCleaningStatus());
        aExistingBed.setExpirationDate(iUpdatedBed.getExpirationDate());
        aExistingBed.setPhysicalState(iUpdatedBed.getPhysicalState());
        aExistingBed.setRemainingLifeSpan(iUpdatedBed.getRemainingLifeSpan());
        aExistingBed.setPoids(iUpdatedBed.getPoids());
        //  aExistingBed.setRoomBed(iUpdatedBed.getRoomBed());
        bedRespository.save(aExistingBed);
    }


    @Override
    public void delete(Integer iBedKey) {
        bedRespository.deleteById(iBedKey);
    }

    @Override
    public Bed getBedbyNumber(Integer iBedNumber) {
        return bedRespository.findByBedNumber(iBedNumber);
    }

    @Override
    public List<Bed> getBedByType(BedType iBedType) {
        return bedRespository.findByBedType(iBedType);


    }

    @Override
    public List<Bed> getByStatue(BedStatus iBedStatue) {
        return bedRespository.findByBedStatue(iBedStatue);
    }

    @Override
    public List<Bed> getByphysical_state(BedPhysicalCondition iPhysicalState) {
        return bedRespository.findByPhysicalState(iPhysicalState);
    }

    @Override
    public void updateBed(Bed iBed) {
        Bed aExistingBed = bedRespository.findById(iBed.getBedKey()).orElse(null);
        if (aExistingBed != null) {
            Bed aBedWithSameName = bedRespository.findByBedNumberAndRoomBed(iBed.getBedNumber(), aExistingBed.getRoomBed());
            if (aBedWithSameName != null && !aBedWithSameName.getBedKey().equals(iBed.getBedKey())) {
                throw new IllegalStateException("Le nom de cet lit  déja existe , donner un nom unique !");
            }else{


            aExistingBed.setBedType(iBed.getBedType());
            aExistingBed.setBedCleaningStatus(iBed.getBedCleaningStatus());
            aExistingBed.setPhysicalState(iBed.getPhysicalState());
            aExistingBed.setPoids(iBed.getPoids());
            aExistingBed.setBedDescription(iBed.getBedDescription());
            aExistingBed.setBedPurchaseDate(iBed.getBedPurchaseDate());
            aExistingBed.setExpirationDate(iBed.getExpirationDate());
            //aExistingBed.setRoomBed(iBed.getRoomBed());
            aExistingBed.setEquipmentList(iBed.getEquipmentList());
            aExistingBed.setPoids(iBed.getPoids());
            aExistingBed.setBedNumber(iBed.getBedNumber());
            aExistingBed.setRemainingLifeSpan(iBed.getRemainingLifeSpan());
            bedRespository.save(aExistingBed);
          }
        }
        else {
            throw new IllegalStateException("Aucun lit existe avec cet ID"); //
        }


    }

    @Override
    public void deleteBed(Bed iBed) {
        bedRespository.delete(iBed);
    }

    @Override
    public Set<BedEquipLink> getEquipmentsBybed(Integer iCareUnitId) {
        Bed aBed = bedRespository.findById(iCareUnitId).orElseThrow(() -> new RuntimeException("Aucun lit existe avec cet ID"));
        return aBed.getBedEquipLinkSet();
    }


    @Override
    public void addEquipmentsToBed(BedEquipLink iAssociation) {
        bedEquiLinkRepo.save(iAssociation);
    }


    @Override
    @Transactional
    public void updateEquipmentInBed(Integer iBedKey, List<Long> iOldEquipmentList, List<Long> iNewEquipmentList) {
        Bed abed = bedRespository.findById(iBedKey)
                .orElseThrow(() -> new IllegalArgumentException("Aucun lit existe avec cet ID"));

        for (int i = 0; i < iOldEquipmentList.size(); i++) {
            Long aOldEquipmentId = iOldEquipmentList.get(i);
            Long aNewEquipmentId = iNewEquipmentList.get(i);

            // Check if the old equipment ID exists in the CareUnitEquipLink association
            BedEquipLink aBedEquipLink = abed.getBedEquipLinkSet().stream()
                    .filter(link -> link.getEquipment().getEquipementkey().equals(aOldEquipmentId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("cet equipement n'existe pas dans cet lit"));

            // Check if the new equipment ID exists in the equipment table
            Equipment aNewEquipment = equipementRepository.findById(aNewEquipmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Aucun equipement existe avec cet ID"));

            // Update the equipment ID in the association
            aBedEquipLink.setEquipment(aNewEquipment);
        }

        // Save the updated CareUnit entity
        bedRespository.save(abed);
    }

    @Override
    @Transactional
    public void removeEquipmentFrombed(Integer iBedKey, Long iEquipmentId) {
        // Vérifier si l'association entre l'unité de soins et l'équipement existe
        List<BedEquipLink> aAssociations = bedEquiLinkRepo.findByBed_BedKeyAndEquipment_Equipementkey(iBedKey, iEquipmentId);
        if (!aAssociations.isEmpty()) {
            BedEquipLink aAssociationToDelete = aAssociations.get(0);
            bedEquiLinkRepo.delete(aAssociationToDelete);

        } else {
            // Lancer une exception si l'association n'existe pas
            throw new IllegalArgumentException("Aucune association existe entre l'equipement et cet lit ");
        }

    }
}