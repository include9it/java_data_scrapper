package io.saltpay.model;

public class ProcuratorPhones {
    private final String fullName;
    private final PhoneNumbers phoneNumbers;

    public ProcuratorPhones(String fullName, PhoneNumbers phoneNumbers) {
        this.fullName = fullName;
        this.phoneNumbers = phoneNumbers;
    }

    public String getFullName() {
        return fullName;
    }

    public PhoneNumbers getPhoneNumbers() {
        return phoneNumbers;
    }
}
