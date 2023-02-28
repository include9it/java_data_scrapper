package io.saltpay.model;

import java.io.Serializable;
import java.util.List;

public class SsnData implements Serializable {
    private final String ssnValue;
    private final List<Procurator> listOfProcurator;

    public SsnData(String ssnValue, List<Procurator> listOfProcurator) {
        this.ssnValue = ssnValue;
        this.listOfProcurator = listOfProcurator;
    }

    public String getSsnValue() {
        return ssnValue;
    }

    public List<Procurator> getListOfProcurator() {
        return listOfProcurator;
    }
}
