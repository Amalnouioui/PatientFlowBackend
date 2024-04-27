package com.core.Parameterization.Respositories;

import com.core.Parameterization.Entities.Bed;
import com.core.Parameterization.Entities.BedLocked;
import com.core.Parameterization.Entities.Enumeration.OccupantType;
import com.core.Parameterization.Entities.Enumeration.RoomType;
import com.core.Parameterization.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Repository
public interface BedLockedRepository extends JpaRepository<BedLocked,Integer> {


    BedLocked findByBedLockedOccupantKy(Integer bedLocked);
    BedLocked findByBedLockedOccupantKyAndAndBedLockedOccupantType(Integer bedLockedKy,OccupantType occupantType);
    BedLocked findByBed_BedKey(Integer bedKey);

}
