package com.core.patient.entities.enumeration;

public enum IdentityType {
	ID_Card("ID_Card"),
    Passport("Passport"),
    Residence_Permit("Residence_Permit");
	
	private final String displayName;

	IdentityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
