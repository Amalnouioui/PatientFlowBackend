package com.core.Parameterization.Entities.Enumeration;

public enum UnitType {
    Medecine_General("GeneralMedicine"),
    Cherugie("Surgery"),
    Pediatrie("Pediatrics"),
    Gynecologie("Gynecology");

    private final String value;

    UnitType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
