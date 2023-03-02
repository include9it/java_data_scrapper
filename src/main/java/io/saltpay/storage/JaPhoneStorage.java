package io.saltpay.storage;

import io.saltpay.models.ProcuratorPhones;

import java.util.ArrayList;
import java.util.List;

public class JaPhoneStorage {

    private Boolean hasSavedData = null;

    public void saveProcuratorPhoneData(String fileName, ProcuratorPhones phones) {
        List<ProcuratorPhones> phonesList = new ArrayList<>();
        phonesList.add(phones);

        if (checkHasSavedData(fileName)) {
            JaPhoneFileManager.appendObjectsToFile(fileName, phonesList);

            return;
        }

        JaPhoneFileManager.writeObjectsToFile(fileName, phonesList);

        hasSavedData = true;
    }

    public void saveProcuratorPhoneData(String fileName, List<ProcuratorPhones> phonesList) {
        if (checkHasSavedData(fileName)) {
            JaPhoneFileManager.appendObjectsToFile(fileName, phonesList);

            return;
        }

        JaPhoneFileManager.writeObjectsToFile(fileName, phonesList);

        hasSavedData = true;
    }

    public List<ProcuratorPhones> readSavedPhonesData(String fileName) {
        if (checkHasSavedData(fileName)) {
            return JaPhoneFileManager.readObjectsFromFile(fileName);
        }

        return null;
    }

    private boolean checkHasSavedData(String fileName) {
        if (hasSavedData == null) {
            hasSavedData = JaPhoneFileManager.readObjectsFromFile(fileName) != null;
        }

        return hasSavedData;
    }
}
