package com.core.Parameterization.Entities.Enumeration;

public enum UnitType {
    Intensifs("Unité de soins intensifs"),
  Intermediaires("Unité de soins intermédiaires"),
  A_Long_Terme("A Long Terme");


    private final String value;

    UnitType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
