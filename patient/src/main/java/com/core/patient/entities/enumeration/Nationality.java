package com.core.patient.entities.enumeration;

public enum Nationality {
	
	Tunisien("Tunisien"),
    Américain("Américain"),
    Britannique("Britannique"),
    Français("Français"),
    Allemand("Allemand"),
    Italien("Italien"),
    Japonais("Japonais");
	

     private final String displayName;

     Nationality(String displayName) {
         this.displayName = displayName;
     }

     public String getDisplayName() {
         return displayName;
     }

}
