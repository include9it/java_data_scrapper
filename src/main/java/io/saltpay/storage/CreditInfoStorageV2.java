package io.saltpay.storage;

import io.saltpay.models.SsnData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreditInfoStorageV2 {

    private final StorageController storageController = new StorageController();

    public void saveSsnData(String fileName, SsnData ssnData) {
        storageController.saveData(fileName, ssnData);
    }

    public void saveSsnData(String fileName, List<SsnData> ssnDataList) {
        storageController.saveData(fileName, ssnDataList);
    }

    public List<SsnData> readSavedSsnData(String fileName) {
        return storageController.readData(fileName);
    }
}
