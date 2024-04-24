package com.core.Parameterization.Entities.Enumeration;

public enum CompanionRelation {
    Conjoint("Conjoint"),
    Parent("Parent "),
    Enfant("Enfant"),
    Frére("Frére "),
    Soeur("Soeur "),
    Ami("Ami "),
    Unconnu ("Uknown");

    private final String displayName;

    CompanionRelation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
