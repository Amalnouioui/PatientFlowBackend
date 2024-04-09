package com.core.patient.entities.enumeration;

public enum Country {

	 Tunisie("Tunisie"),
	 États_Unis("États-Unis"),
	 Canada("Canada"),
	 France("France"),
	 Allemagne("Allemagne"),
	 Japon("Japon");
    
    private final String displayName;

	Country(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
