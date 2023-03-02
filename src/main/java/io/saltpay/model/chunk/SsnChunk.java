package io.saltpay.model.chunk;

import java.util.List;

public class SsnChunk {
    private final List<String> listOfSsn;

    public SsnChunk(List<String> listOfSsn) {
        this.listOfSsn = listOfSsn;
    }

    public List<String> getListOfSsn() {
        return listOfSsn;
    }
}
