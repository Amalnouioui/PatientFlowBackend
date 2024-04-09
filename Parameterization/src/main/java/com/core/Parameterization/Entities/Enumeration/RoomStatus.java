package com.core.Parameterization.Entities.Enumeration;

public enum RoomStatus {

    Disponible("Chambre disponible"),
    Occupe("Chambre occupé "),
    Reserve("Chambre reservé"),
    En_Maintenance("Chambre en maintenance");


    private final String displayName;
    RoomStatus(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {

        return displayName;
    }
}
