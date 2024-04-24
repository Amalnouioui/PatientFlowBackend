package com.core.Parameterization.Entities.Enumeration;

public enum BedType {

        Simple("Lit Simple"),
        Medicalise("Lit  médicalisé");

        private final String displayName;

        BedType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

}
