package com.core.Parameterization.Entities.Enumeration;

public enum BedPhysicalCondition {

        Bon_Etat("bon etat"),
         Besoin_De_Reparation_Mineur("Besoin de réparation mineur"),
         Besoin_De_Reparation_Majeur("Besoin de réparation majeur");

        private final String displayName;

        BedPhysicalCondition(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

    }


