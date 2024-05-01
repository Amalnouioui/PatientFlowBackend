package com.core.Parameterization.Services;

import com.core.Parameterization.Entities.Bed;
import com.core.Parameterization.Entities.BedLocked;
import com.core.Parameterization.Entities.Enumeration.BedType;
import com.core.Parameterization.Entities.Room;

import java.util.Date;
import java.util.Optional;

public interface BedLockedService {
    void addPerson (BedLocked bedLocked);
   void adPerssonToBed(Integer roomKey, BedLocked bedLocked, boolean accompagnement,double patientWeight);
   void saveBedLocked (BedLocked iBedLocked);
    BedLocked getBedLockedByKey(Integer iBedLocked);
    Optional<BedLocked>  findById( Integer id);

    boolean patientCheck(Integer id);
  void   changePatientReakEnteryDate(Integer id, Date bedLocked_RealUnxTmBgn);

    Bed changePatientBed(Integer roomKey, BedLocked bedLocked, boolean accompagnement, double patientWeight, Integer commpanionKey);

   Room getPatientRoom(Integer patientKey);

   BedLocked getBedLockedcompanion(Integer comapnionKey);
Bed getBedbyType(BedType type);
    boolean checkAccompagnant(Integer oldroomKey) ;
    Integer getAccompagnat(Integer patientKey);
    //void prolongation(Integer id, Date bedLocked_PlannedUnxTmEnd) ;

    void libererPatientFromBed(Integer patientKey);
   void  addRealExitDate(Integer id, Date bedLocked_RealUnxTmEnd);



}
