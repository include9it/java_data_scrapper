package io.saltpay.storage;

import io.saltpay.models.ProcuratorPhones;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JaPhoneSaveManager {

    private Boolean hasSavedData = null;

    public void saveProcuratorPhoneData(String fileName, ProcuratorPhones phones) {
        List<ProcuratorPhones> phonesList = new ArrayList<>();
        phonesList.add(phones);

        if (checkHasSavedData(fileName)) {
            FileManager.appendObjectsToFile(fileName, phonesList);

            return;
        }

        FileManager.writeObjectsToFile(fileName, phonesList);

        hasSavedData = true;
    }

    public void saveProcuratorPhoneData(String fileName, List<ProcuratorPhones> phonesList) {
        if (checkHasSavedData(fileName)) {
            FileManager.appendObjectsToFile(fileName, phonesList);

            return;
        }

        FileManager.writeObjectsToFile(fileName, phonesList);

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
            hasSavedData = FileManager.readObjectsFromFile(fileName) != null;
        }

        return hasSavedData;
    }

    private List<ProcuratorPhones> readAndMapSavedData(String fileName) {
        List<Object> objectList = FileManager.readObjectsFromFile(fileName);

        return objectList.stream()
                .map(object -> (ProcuratorPhones) object)
                .collect(Collectors.toList());
    }
}
