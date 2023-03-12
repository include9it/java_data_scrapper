package io.saltpay.storage;

import io.saltpay.models.ProcuratorPhones;

import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.DATA_MODEL_FILE_EXTENSION;
import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class JaPhoneStorage {

    private final StorageController storageController;

    public JaPhoneStorage(String fileName) {
        storageController = new StorageController(getFilePathWithExtension(fileName));
    }

    public void saveProcuratorPhoneData(ProcuratorPhones phones) {
        List<ProcuratorPhones> phonesList = new ArrayList<>();
        phonesList.add(phones);

        storageController.saveData(phonesList);
    }

    public void saveProcuratorPhoneData(List<ProcuratorPhones> phonesList, boolean override) {
        storageController.saveData(phonesList);
    }

    public List<ProcuratorPhones> readSavedPhonesData() {
        return storageController.readData();
    }

    private static String getFilePathWithExtension(String fileName) {
        return RESOURCE_FILE_PATH + fileName + DATA_MODEL_FILE_EXTENSION;
    }
}
