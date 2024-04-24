package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.Enumeration.RoomStatus;
import com.core.Parameterization.Entities.Enumeration.UnitStatus;
import com.core.Parameterization.Entities.Enumeration.RoomType;
import com.core.Parameterization.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
   Room findRoomByRoomName(Integer roomName);
   List<Room>findByroomType(RoomType roomType);
   List<Room>findByroomStatue(RoomStatus roomStatue);
   Room findByRoomNameAndCareunitRoom_CareunitKey(Integer roomName, Integer careunitKey);
   List<Room>findByCareunitRoom_CareunitKeyAndRoomType(Integer CareunitKey,RoomType room);

}
