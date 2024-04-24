package com.core.patient.entities.enumeration;

public enum BloodPressure {
    Normal("Normal"),
    Low("Normal"),
    High("High");


    private final String displayName;

    BloodPressure(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
