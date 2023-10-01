package io.saltpay.models;

import java.io.Serializable;

public record Procurator(String fullName, String personalCode) implements Serializable {
}
