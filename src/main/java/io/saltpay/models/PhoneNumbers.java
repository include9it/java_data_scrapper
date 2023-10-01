package io.saltpay.models;

import java.io.Serializable;

public record PhoneNumbers(String phone1, String phone2) implements Serializable {
}
