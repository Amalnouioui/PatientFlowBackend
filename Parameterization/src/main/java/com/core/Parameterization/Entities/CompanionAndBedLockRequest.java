package com.core.Parameterization.Entities;

public class CompanionAndBedLockRequest {

private RoomCompanion roomCompanion;
    private BedLocked bedLocked;

    public RoomCompanion getRoomCompanion() {
        return roomCompanion;
    }

    public void setRoomCompanion(RoomCompanion roomCompanion) {
        this.roomCompanion = roomCompanion;
    }

    public BedLocked getBedLocked() {
        return bedLocked;
    }

    public void setBedLocked(BedLocked bedLocked) {
        this.bedLocked = bedLocked;
    }
}
