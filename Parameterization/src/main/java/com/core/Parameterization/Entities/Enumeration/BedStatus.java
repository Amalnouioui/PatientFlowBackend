package com.core.Parameterization.Entities.Enumeration;

public enum BedStatus {

    Disponible("Lit disponible"),
    Occupe("Lit occupé "),
    Reserve("Lit reservé"),
    En_Maintenance("Lit en maintenance");


    private final String displayName;
    BedStatus(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {

        return displayName;
    }
}
