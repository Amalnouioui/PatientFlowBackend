package com.core.Parameterization.Services;

import com.core.Parameterization.Entities.Bed;
import com.core.Parameterization.Entities.Enumeration.RoomStatus;
import com.core.Parameterization.Entities.Enumeration.UnitStatus;
import com.core.Parameterization.Entities.Enumeration.RoomType;
import com.core.Parameterization.Entities.Room;

import java.util.*;


public interface RoomService {
    void create(Room iRoom);
    List<Room>retreiveRooms();
    Optional<Room>getRoomByKey(Integer iRoomkey);
    Room getRoombyItsName(Integer iRoomName);
    void delete(Integer iRoomKey);
    void update( Integer iRoomKey,Room iRoom);
    List<Room>getRoomByType(RoomType iRoomType);
    List<Room>getRoomByStatue(RoomStatus iRoomStatue);

    List<Bed>getBed(Room iRoom);

    void addBed(Room iRoom, Bed iBed);


    void   deleteRoom(Room iRoom);
    void updateRoom(Room iRoom);


}