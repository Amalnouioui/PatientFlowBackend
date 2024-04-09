package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.Bed;
import com.core.Parameterization.Entities.Enumeration.BedPhysicalCondition;
import com.core.Parameterization.Entities.Enumeration.BedStatus;
import com.core.Parameterization.Entities.Enumeration.BedType;
import com.core.Parameterization.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository


public interface BedRespository extends JpaRepository<Bed,Integer> {
    Bed findByBedNumber(Integer bedNumber);
    List<Bed> findByBedType(BedType bedType);
    List<Bed> findByBedStatue(BedStatus bedStatue);
    List<Bed> findByPhysicalState(BedPhysicalCondition physicalState);
    Bed findByBedNumberAndRoomBed(Integer bedNumber, Room room);

}
