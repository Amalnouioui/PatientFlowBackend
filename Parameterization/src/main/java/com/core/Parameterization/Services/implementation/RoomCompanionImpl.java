package com.core.Parameterization.Services.implementation;


import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.BedStatus;
import com.core.Parameterization.Entities.Enumeration.BedType;
import com.core.Parameterization.Respositories.BedLockedRepository;
import com.core.Parameterization.Respositories.BedRespository;
import com.core.Parameterization.Respositories.RoomCompanionRepository;
import com.core.Parameterization.Respositories.RoomRepository;
import com.core.Parameterization.Services.RoomCompanionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomCompanionImpl implements RoomCompanionService {

    @Autowired
    private RoomCompanionRepository roomCompanionRepository;

@Autowired
private BedLockedRepository bedLockedRepository;
@Autowired
private RoomRepository roomRepository;
@Autowired
private BedRespository bedRespository;

        @Override
        public void addCompanion(RoomCompanion roomCompanion){
        roomCompanionRepository.save(roomCompanion);

        }
        @Override
        public Optional<RoomCompanion> getCompanionById(Integer id){
            return roomCompanionRepository.findById(id);
        }
        @Override
        public Optional<RoomCompanion> getCompanionbyName(String name){
            return roomCompanionRepository.findByRoomCompanionName(name);
        }

    public void reservationcompanion(Integer roomKey, CompanionAndBedLockRequest request) {
        RoomCompanion accompagnant = request.getRoomCompanion();
        roomCompanionRepository.save(accompagnant);


        // Récupération de l'ID de l'accompagnant
        Integer accompagnantId = accompagnant.getRoomCompanionKy();
        System.out.println(accompagnantId);
        // Récupération du BedLocked à partir de la requête
        BedLocked bedLocked = request.getBedLocked();

        // Attribution de l'ID de l'accompagnant au BedLocked
        bedLocked.setBedLockedOccupantKy(accompagnantId);
        bedLockedRepository.save((bedLocked));
        // Récupération de la chambre à partir de la base de données
        Optional<Room> aRoomOptional = roomRepository.findById(roomKey);
        if (aRoomOptional.isPresent()) {
            Room room = aRoomOptional.get();
            List<Bed> bedList = room.getRoomBed();
            for (Bed bed : bedList) {
                if (bed.getBedType() == BedType.Simple) {
                    // Attribution du lit au BedLocked
                    bedLocked.setBed(bed);

                    // Enregistrement du BedLocked
                    bedLockedRepository.save(bedLocked);

                    // Mise à jour du statut du lit et sauvegarde
                    bed.setBedStatue(BedStatus.Reserve);
                    bed.setRoomBed(room);
                    bedRespository.save(bed);
                }
            }
        }
    }









}
