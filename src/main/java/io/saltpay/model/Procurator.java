package io.saltpay.model;

import java.io.Serializable;

public class Procurator implements Serializable {
    private final String fullName;
    private final String personalCode;

    public Procurator(String fullName, String personalCode) {
        this.fullName = fullName;
        this.personalCode = personalCode;
    }

    public String getFullName() {
        return fullName;
    }
    public String getPersonalCode() {
        return personalCode;
    }
}
