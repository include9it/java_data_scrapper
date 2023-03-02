package io.saltpay.storage;

import io.saltpay.models.SsnData;

import java.util.ArrayList;
import java.util.List;

public class CreditInfoSaveManager {

    private Boolean hasSavedData = null;

    public void saveSsnData(String fileName, SsnData ssnData) {
        if (checkHasSavedData(fileName)) {
            FileManager.appendModelToFile(fileName, ssnData);

            return;
        }

        FileManager.writeModelToFile(fileName, ssnData);

        hasSavedData = true;
    }

    public void saveSsnData(String fileName, List<SsnData> ssnDataList) {
        if (checkHasSavedData(fileName)) {
            FileManager.appendModelToFile(fileName, ssnDataList);

            return;
        }

        FileManager.writeModelToFile(fileName, ssnDataList);

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
            hasSavedData = FileManager.readModelsFromFile(fileName) != null;
        }

        return hasSavedData;
    }

    private List<SsnData> readAndMapSavedData(String fileName) {
        List<Object> listOfObjects = FileManager.readModelsFromFile(fileName);

        List<SsnData> listOfSsnData = new ArrayList<>();

        listOfObjects.forEach(object -> listOfSsnData.add((SsnData) object));

        return listOfSsnData;
    }
}
