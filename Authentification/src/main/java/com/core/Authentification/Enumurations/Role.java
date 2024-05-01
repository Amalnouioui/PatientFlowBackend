package com.core.Authentification.Enumurations;

public enum Role {
    User("User"),
    Doctor("Doctor"),
    Admin("Admin"),
    All("All"),
    Patient("Patient");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
