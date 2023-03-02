package io.saltpay.storage;

import io.saltpay.models.SsnData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreditInfoSaveManager {

    private Boolean hasSavedData = null;

    public void saveSsnData(String fileName, SsnData ssnData) {
        List<SsnData> dataList = new ArrayList<>();
        dataList.add(ssnData);

        if (checkHasSavedData(fileName)) {
            FileManager.appendObjectsToFile(fileName, dataList);

            return;
        }

        FileManager.writeObjectsToFile(fileName, dataList);

        hasSavedData = true;
    }

    public void saveSsnData(String fileName, List<SsnData> ssnDataList) {
        if (checkHasSavedData(fileName)) {
            FileManager.appendObjectsToFile(fileName, ssnDataList);

            return;
        }

        FileManager.writeObjectsToFile(fileName, ssnDataList);

        hasSavedData = true;
    }

    public List<SsnData> readSavedSsnData(String fileName) {
        if (checkHasSavedData(fileName)) {
            return readAndMapSavedData(fileName);
        }

        return null;
    }

    private boolean checkHasSavedData(String fileName) {
        if (hasSavedData == null) {
            hasSavedData = FileManager.readObjectsFromFile(fileName) != null;
        }

        return hasSavedData;
    }

    private List<SsnData> readAndMapSavedData(String fileName) {
        List<Object> objectList = FileManager.readObjectsFromFile(fileName);

        return objectList.stream()
                .map(object -> (SsnData) object)
                .collect(Collectors.toList());
    }
}
