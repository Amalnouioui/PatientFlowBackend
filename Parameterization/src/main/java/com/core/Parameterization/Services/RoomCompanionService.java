package com.core.Parameterization.Services;

import com.core.Parameterization.Entities.CompanionAndBedLockRequest;
import com.core.Parameterization.Entities.RoomCompanion;

import java.util.Optional;

public interface RoomCompanionService {
void  addCompanion(RoomCompanion roomCompanion);

    Optional<RoomCompanion> getCompanionById(Integer id);
    Optional<RoomCompanion> getCompanionbyName(String name);
    void  reservationcompanion(Integer roomKey, CompanionAndBedLockRequest request);


}
