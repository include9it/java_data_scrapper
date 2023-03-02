package io.saltpay.storage;

import io.saltpay.models.SsnData;

import java.util.ArrayList;
import java.util.List;

public class CreditInfoStorage {

    private Boolean hasSavedData = null;

    public void saveSsnData(String fileName, SsnData ssnData) {
        List<SsnData> dataList = new ArrayList<>();
        dataList.add(ssnData);

        if (checkHasSavedData(fileName)) {
            CreditInfoFileManager.appendObjectsToFile(fileName, dataList);

            return;
        }

        CreditInfoFileManager.writeObjectsToFile(fileName, dataList);

        hasSavedData = true;
    }

    public void saveSsnData(String fileName, List<SsnData> ssnDataList) {
        if (checkHasSavedData(fileName)) {
            CreditInfoFileManager.appendObjectsToFile(fileName, ssnDataList);

            return;
        }

        CreditInfoFileManager.writeObjectsToFile(fileName, ssnDataList);

        hasSavedData = true;
    }

    public List<SsnData> readSavedSsnData(String fileName) {
        if (checkHasSavedData(fileName)) {
            return CreditInfoFileManager.readObjectsFromFile(fileName);
        }

        return null;
    }

    private boolean checkHasSavedData(String fileName) {
        if (hasSavedData == null) {
            hasSavedData = CreditInfoFileManager.readObjectsFromFile(fileName) != null;
        }

        return hasSavedData;
    }
}
