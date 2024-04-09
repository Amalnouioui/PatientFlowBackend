package com.core.Parameterization.Entities.Enumeration;

public enum BedType {

        Simple("Lit Simple"),
        Double("Lit  Double"),
        Medicalise("Lit  médicalisé");

        private final String displayName;

        BedType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

}
