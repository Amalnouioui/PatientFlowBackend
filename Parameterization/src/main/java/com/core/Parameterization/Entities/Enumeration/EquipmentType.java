package com.core.Parameterization.Entities.Enumeration;

public enum EquipmentType {
    Unité_de_soins("Unité de soins"),
    Lit("Lit ");


    private final String displayName;
    EquipmentType(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {

        return displayName;
    }
}
