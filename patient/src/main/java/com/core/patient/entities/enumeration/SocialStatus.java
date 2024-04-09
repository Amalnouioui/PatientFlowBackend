package com.core.patient.entities.enumeration;

public enum SocialStatus {
	Célibataire("Célibataire"),
    Marié("Marié"),
    Divorcé("Divorcé"),
    Veuf("Veuf");
	
	 private final String displayName;

	 SocialStatus(String displayName) {
	        this.displayName = displayName;
	    }

	    public String getDisplayName() {
	        return displayName;
	    }

}
