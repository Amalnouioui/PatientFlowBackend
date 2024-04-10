package com.core.Parameterization.Services;

import com.core.Parameterization.Entities.*;
import com.core.Parameterization.Entities.Enumeration.ServiceType;
import com.core.Parameterization.Entities.Enumeration.UnitStatus;
import com.core.Parameterization.Entities.Enumeration.UnitType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public  interface CareUnitService {


     CareUnit  create (CareUnit iCareUnit);
     List<CareUnit>retreivesAllCareUnits();
     Optional<CareUnit> getCareUnitByKey(Integer iCareunitKey);



    CareUnit  getCareUnitbyNme(String iCareunitName);
    void update(Integer iCareunitKey,CareUnit iCareUnit);
    void delete(Integer iCareunitKey);
    List<Room>getRoom(CareUnit iCareUnit);
    void addRoom(CareUnit iCareUnit,Room iRoom);
    void updateCareUnit(CareUnit iCareUnit);
    List<CareUnit> getCareUnitByStatue(UnitStatus iCareunitStatue);
   Set<CareUnitEquipLink> getEquipmentsByCareUnit(Integer iCareUnitId);
   // void addEquipmentToCareUnit(Integer careUnitId, CareUnitEquipLink equipment);
    void removeEquipmentFromCareUnit(Integer iCareUnitId, Long iEquipmentId);
    void addEquipmentToCareUnit(CareUnitEquipLink iCareUnitEquipLink);
    //void updateEquipmentInCareUnit(Integer careUnitId, List<Long> updatedEquipmentIds);
   // void addServices(CareUnitServiceLink iCareUnitServiceLink);


   // void updateEquipmentInCareUnit(Integer iCareUnitId, List<Long> iOldEquipmentList, List<Long> iNewEquipmentList);

    //void updateService(Integer iCareUnitId,  List<Long>iOldServiceList, List<Long> iNewServiceList);
    //Set<CareUnitServiceLink> getServicesByCareUnit(Integer iCareunitKey);

  //  void removeService(Integer iCareUnitId, Long iServiceKey);
    List<CareUnit> searchByCriteria(int careunitCapacity, UnitType careuniType, UnitStatus careunitStatue);
    List<ServiceEntity>getServicebyType (ServiceType serviceType);
}
