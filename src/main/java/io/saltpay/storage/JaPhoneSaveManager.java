package io.saltpay.storage;

import io.saltpay.model.ProcuratorPhones;

import java.util.ArrayList;
import java.util.List;

public class JaPhoneSaveManager {

    private Boolean hasSavedData = null;

    public void saveProcuratorPhoneData(String fileName, ProcuratorPhones phones) {
        if (checkHasSavedData(fileName)) {
            FileManager.appendModelToFile(fileName, phones);

            return;
        }

        FileManager.writeModelToFile(fileName, phones);

        hasSavedData = true;
    }

    public void saveProcuratorPhoneData(String fileName, List<ProcuratorPhones> phonesList) {
        if (checkHasSavedData(fileName)) {
            FileManager.appendModelToFile(fileName, phonesList);

            return;
        }

        FileManager.writeModelToFile(fileName, phonesList);

        hasSavedData = true;
    }

    public List<ProcuratorPhones> readSavedPhonesData(String fileName) {
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

    private List<ProcuratorPhones> readAndMapSavedData(String fileName) {
        List<Object> listOfObjects = FileManager.readModelsFromFile(fileName);

        List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

        listOfObjects.forEach(object -> listOfProcuratorPhones.add((ProcuratorPhones) object));

        return listOfProcuratorPhones;
    }
}
