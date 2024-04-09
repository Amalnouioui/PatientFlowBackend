package com.core.Parameterization.Entities.Enumeration;

public enum RoomType {
    Simple("Chambre Simple"),
    Double("Chambre Double"),
    COLLECTIVE("Chambre Collective ");


    private final String displayName;
    RoomType(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {

        return displayName;
    }
}
