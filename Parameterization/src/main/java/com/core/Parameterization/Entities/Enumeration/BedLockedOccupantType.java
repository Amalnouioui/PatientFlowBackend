package com.core.Parameterization.Entities.Enumeration;

public enum BedLockedOccupantType {
    Patient("Patient"),
    RoomCompanion("RoomCompanion");

    private final String displayName;

    BedLockedOccupantType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
