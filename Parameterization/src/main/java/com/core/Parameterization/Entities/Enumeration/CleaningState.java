package com.core.Parameterization.Entities.Enumeration;

public enum CleaningState {
    Nettoye("Nettoyé"),
    En_cours_De_Nettoyage("En cours de nettoyage"),
    A_Nettoyer("A nettoyer ");

    private final String displayName;

    CleaningState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
