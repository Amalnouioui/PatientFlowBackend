package com.core.Parameterization.Services;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.BedPhysicalCondition;
import com.core.Parameterization.Entities.Enumeration.BedStatus;
import com.core.Parameterization.Entities.Enumeration.BedType;
import com.core.Parameterization.Entities.Enumeration.UnitStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BedService {

    List<Bed> retrieveBeds();

    void create(Bed iBed);
    Optional<Bed> getBedByKey(Integer iBedKy);
    void update(Integer iBedKey, Bed iUpdatedBed);
    void delete(Integer iBedKey);
    Bed getBedbyNumber(Integer iBedNumber) ;
    List<Bed> getBedByType(BedType iBedType) ;
   List<Bed> getByStatue(BedStatus iBedStatue);
   List<Bed> getByphysical_state(BedPhysicalCondition iPhysicalState);

    void updateBed(Bed iNewBed);
    void deleteBed(Bed iBed);

    Set<BedEquipLink> getEquipmentsBybed(Integer iCareUnitId);


    void addEquipmentsToBed(BedEquipLink iBedEquipLink);

    void updateEquipmentInBed(Integer iCareUnitId, List<Long> iOldEquipmentList, List<Long> iNewEquipmentList);
    void removeEquipmentFrombed(Integer iBedKey, Long iEquipmentId);




}
