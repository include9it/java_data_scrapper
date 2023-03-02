package io.saltpay.model;

public class PhoneNumbers {
    private final String phone1;
    private final String phone2;

    public PhoneNumbers(String phone1, String phone2) {
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }
}
