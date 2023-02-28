package io.saltpay.model;

public class Procurator extends Person {

    private final String personalCode;

    public Procurator(String fullName, String personalCode) {
        super(fullName);

        this.personalCode = personalCode;
    }

    public String getPersonalCode() {
        return personalCode;
    }
}
