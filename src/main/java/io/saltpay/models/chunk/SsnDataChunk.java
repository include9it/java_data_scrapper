package io.saltpay.models.chunk;

import io.saltpay.models.SsnData;

import java.util.List;

public class SsnDataChunk {
    private final List<SsnData> listOfSsnData;

    public SsnDataChunk(List<SsnData> listOfSsnData) {
        this.listOfSsnData = listOfSsnData;
    }

    public List<SsnData> getListOfSsnData() {
        return listOfSsnData;
    }
}
