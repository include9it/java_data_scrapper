package io.saltpay.models;

import java.io.Serializable;

public record ProcuratorPhones(String fullName, PhoneNumbers phoneNumbers) implements Serializable {
}
