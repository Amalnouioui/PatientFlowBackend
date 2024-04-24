package com.core.Parameterization.Entities.Enumeration;

public enum OccupantType {
    Patient("Patient"),
    Accompagnant("Accompagnant");
    private final String displayName;

    OccupantType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
