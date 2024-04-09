package com.core.patient.entities.enumeration;

public enum Gender {
	 Homme("Homme"),
     Femme("Femme"),
     Autre("Autre ");

     private final String displayName;

     Gender(String displayName) {
         this.displayName = displayName;
     }

     public String getDisplayName() {
         return displayName;
     }

}
