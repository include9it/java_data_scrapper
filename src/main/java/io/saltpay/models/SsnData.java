package io.saltpay.models;

import java.io.Serializable;
import java.util.List;

public record SsnData(String ssnValue, List<Procurator> listOfProcurator) implements Serializable {
}
