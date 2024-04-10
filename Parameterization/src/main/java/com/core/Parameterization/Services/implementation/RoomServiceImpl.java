package com.core.Parameterization.Services.implementation;

import com.core.Parameterization.Entities.Bed;
import com.core.Parameterization.Entities.CareUnit;
import com.core.Parameterization.Entities.Enumeration.BedType;
import com.core.Parameterization.Entities.Enumeration.RoomStatus;
import com.core.Parameterization.Entities.Enumeration.UnitStatus;
import com.core.Parameterization.Entities.Enumeration.RoomType;
import com.core.Parameterization.Entities.Room;
import com.core.Parameterization.Respositories.CareUnitRepository;
import com.core.Parameterization.Respositories.RoomRepository;
import com.core.Parameterization.Services.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
 public  class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;
    @Autowired
    private CareUnitRepository careUnitRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository , CareUnitRepository careUnitRepository){
        this.roomRepository=roomRepository;
        this.careUnitRepository=careUnitRepository;
    }


    @Override
    public void create(Room iRoom) {
        // Vérification de l'existence de l'unité de soins associée à la chambre
        Optional<CareUnit> aCareUnitOptional = careUnitRepository.findById(iRoom.getCareunitRoom().getCareunitKey());

        if (aCareUnitOptional.isPresent()) {
            // Vérification de la capacité en fonction du type de chambre
            int aRoomCapacity = iRoom.getRoomCapacity();

            if (iRoom.getRoomType() == RoomType.Simple && aRoomCapacity != 1) {
                throw new IllegalStateException("La capacité d'une chambre simple doit être de 1");
            }
            else if (iRoom.getRoomType() == RoomType.Double && aRoomCapacity != 2 ) {
                throw new IllegalStateException("La capacité d'une chambre double doit être de 2");
            }
            else if (iRoom.getRoomType() == RoomType.COLLECTIVE && aRoomCapacity < 3) {
                throw new IllegalStateException("La capacité d'une chambre collective doit être d'au moins 3");
            }
            // Vérification de l'unicité du nom de la chambre au sein de l'unité de soins
            Room aExistingRoom = roomRepository.findByRoomNameAndCareunitRoom_CareunitKey(iRoom.getRoomName(), iRoom.getCareunitRoom().getCareunitKey());

            if (aExistingRoom == null) {
                // Le nom de la chambre est unique dans cette unité de soins, enregistrer la chambre
                roomRepository.save(iRoom);
            } else {
                // Le nom de la chambre existe déjà dans l'unité de soins
                throw new IllegalStateException("Le nom de la chambre existe déjà, veuillez donner un nom unique !");
            }
        } else {
            // Aucune unité de soins avec l'identifiant spécifié n'a été trouvée
            throw new IllegalStateException("Aucune unité avec cet identifiant n'a été trouvée");
        }
    }

    @Override
    public List<Room>retreiveRooms(){
        List<Room>aRoomList=roomRepository.findAll();
        return aRoomList;
    }
    @Override
    public Optional<Room>getRoomByKey(Integer iRoomkey){
        return  roomRepository.findById(iRoomkey);
    }
    @Override
    public Room getRoombyItsName(Integer iRoomName){
       return  roomRepository.findRoomByRoomName(iRoomName);

    }
    @Override
    public  void delete(Integer iRoomKey){
        Optional<Room> room=roomRepository.findById(iRoomKey);
        if(room.isPresent()){
            roomRepository.deleteById(iRoomKey);
        }else{
            throw  new IllegalStateException("Aucune chambre avec cet ID !");
        }

    }


    @Override
    public void update(Integer iRoomKey, Room iNewRoom) {
        Room aExistingRoom = roomRepository.findById(iRoomKey).orElseThrow(() -> new EntityNotFoundException("Aucune chambre avec cet ID !"));

        // Check if the new room name is already used in the care unit
        Room aRoomWithSameNameInCareUnit = roomRepository.findByRoomNameAndCareunitRoom_CareunitKey(iNewRoom.getRoomName(), aExistingRoom.getCareunitRoom().getCareunitKey());

        if (aRoomWithSameNameInCareUnit != null && !aRoomWithSameNameInCareUnit.getRoomKey().equals(aExistingRoom.getRoomKey())) {
            throw new IllegalStateException("Le nom du chambre déja existe , donner un nom unique !");
        }

        // Update the room details
        aExistingRoom.setRoomName(iNewRoom.getRoomName());
        aExistingRoom.setRoomCapacity(iNewRoom.getRoomCapacity());
        aExistingRoom.setRoomStatue(iNewRoom.getRoomStatue());
        aExistingRoom.setRoomType(iNewRoom.getRoomType());
        aExistingRoom.setCleaningState(iNewRoom.getCleaningState());
        aExistingRoom.setRoomResponsible(iNewRoom.getRoomResponsible());
        aExistingRoom.setService(iNewRoom.getService());


        roomRepository.save(aExistingRoom);
    }


    @Override
    public List<Room>getRoomByType(RoomType iRoomType){
        List<Room> aRooms=roomRepository.findByroomType(iRoomType);
        return aRooms;
    }
    @Override
    public List<Room>getRoomByStatue(RoomStatus iRoomStatue){
        List<Room> aRoomList=roomRepository.findByroomStatue(iRoomStatue);
        return aRoomList;
    }

    @Override
    public List<Bed> getBed(Room iRoom) {
        return iRoom.getRoomBed();
    }

    @Override
    public void addBed(Room iRoom, Bed iNewBed){
        List<Bed> aBeds = iRoom.getRoomBed();
        int aRoomcapacity=iRoom.getRoomCapacity();
        if(aBeds.size()>=aRoomcapacity){
            throw new IllegalStateException("L'unité de soins est pleine");
        }

        // Vérifier si le nom de la chambre existe déjà dans l'unité de soins
        boolean aBedExistingName = aBeds.stream()
                .anyMatch(bed -> bed.getBedNumber().equals(iNewBed.getBedNumber()));

        // Si le nom de la chambre existe déjà, lancer une IllegalStateException
        if (aBedExistingName) {
            throw new IllegalStateException("Le nom de lit déja existe , donner un nom unique !");
        }
        List<Bed> aBedList=iRoom.getRoomBed();
        aBedList.add(iNewBed);
        iNewBed.setRoomBed(iRoom);
        roomRepository.save(iRoom);
    }



    @Override
    public void deleteRoom(Room iRoom){
        roomRepository.delete(iRoom);
    }




    @Override
    public void updateRoom(Room iRoom) {
        Room aExistingRoom = roomRepository.findById(iRoom.getRoomKey()).orElse(null);

        if (aExistingRoom != null) {
            List<Bed> aBedList = iRoom.getRoomBed();
            int aRoomCapacity = iRoom.getRoomCapacity();



            if (iRoom.getRoomType() == RoomType.Simple && aRoomCapacity != 1) {
                throw new IllegalStateException("Echec de mettre à jour la chambre, la capacité d'une chambre simple ou medicalisée doit être 1 !");
            } else if (iRoom.getRoomType() == RoomType.Double && aRoomCapacity != 2 ) {
                throw new IllegalStateException("Echec de mettre à jour la chambre, la capacité d'une chambre double  doit être 2 !");
            }
            else if (iRoom.getRoomType() == RoomType.COLLECTIVE && aRoomCapacity <3) {
                throw new IllegalStateException("Echec de mettre à jour la chambre, une chambre collective de capacité 2 doit contenir minimum 3");
            }
            else {
                Room aRoomName = roomRepository.findRoomByRoomName(iRoom.getRoomName());
                if ((aRoomName != null) && !aRoomName.getRoomKey().equals(aExistingRoom.getRoomKey())) {
                    throw new IllegalStateException("Le nom de chambre existe déjà, donner un nom unique !");
                } else {
                    aExistingRoom.setRoomName(iRoom.getRoomName());
                    aExistingRoom.setRoomCapacity(iRoom.getRoomCapacity());
                    aExistingRoom.setRoomStatue(iRoom.getRoomStatue());
                    aExistingRoom.setRoomType(iRoom.getRoomType());
                    aExistingRoom.setCleaningState(iRoom.getCleaningState());
                    aExistingRoom.setCareunitRoom(iRoom.getCareunitRoom());
                    aExistingRoom.setRoomResponsible(iRoom.getRoomResponsible());
                    roomRepository.save(aExistingRoom);
                    System.out.println("La chambre a été mise à jour avec succès !");
                }
            }
        } else {
            throw new IllegalStateException("Aucune chambre existe avec cet ID !");
        }
    }



}
