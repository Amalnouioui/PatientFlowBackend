package com.core.Parameterization.Entities.Enumeration;

public enum UnitStatus {

    ACTIVE("Active"),
    InACTIVE("inactive"),
    En_Maintenance("En_maintenance"),
    Ferme_Temporairement("Ferme_temporairement");

    private final String displayName;

    UnitStatus(String displayName) {
        this.displayName=displayName ;
    }

    public  String getDisplayName() {
        return displayName;
    }
 /*public String changeName(String displayName){
        return this.displayName.replace("_"," ");
  }
*/

}
