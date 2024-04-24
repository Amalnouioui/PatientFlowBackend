package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.CareUnit;
import com.core.Parameterization.Entities.Enumeration.RoomType;
import com.core.Parameterization.Entities.Enumeration.UnitStatus;
import com.core.Parameterization.Entities.Enumeration.UnitType;
import com.core.Parameterization.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareUnitRepository extends JpaRepository<CareUnit,Integer> {
 CareUnit findByCareunitName(String careunitName);
 List<CareUnit> findBycareunitStatue(UnitStatus careunitStatue);

    void deleteByRooms(Room room);

    List<CareUnit>findByCareunitCapacityAndCareuniTypeAndCareunitStatue(int careunitCapacity, UnitType careuniType, UnitStatus careunitStatue);

    @Query("SELECT r FROM Room r JOIN r.careunitRoom u WHERE u.careunitKey = :careunitKey AND r.roomType = :roomType")
    List<Room> getRoomsInCareUnit(@Param("careunitKey") Integer careunitKey, @Param("roomType") RoomType roomType);
}
