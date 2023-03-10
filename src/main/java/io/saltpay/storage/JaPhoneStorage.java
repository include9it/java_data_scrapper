package io.saltpay.storage;

import io.saltpay.models.ProcuratorPhones;

import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.DATA_MODEL_FILE_EXTENSION;
import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class JaPhoneStorage {

    private final StorageController storageController = new StorageController();

    public void saveProcuratorPhoneData(String fileName, ProcuratorPhones phones) {
        List<ProcuratorPhones> phonesList = new ArrayList<>();
        phonesList.add(phones);

        storageController.saveData(getFilePathWithExtension(fileName), phonesList);
    }

    public void saveProcuratorPhoneData(String fileName, List<ProcuratorPhones> phonesList, boolean override) {
        storageController.saveData(getFilePathWithExtension(fileName), phonesList);
    }

    public List<ProcuratorPhones> readSavedPhonesData(String fileName) {
        return storageController.readData(getFilePathWithExtension(fileName));
    }

    private static String getFilePathWithExtension(String fileName) {
        return RESOURCE_FILE_PATH + fileName + DATA_MODEL_FILE_EXTENSION;
    }
}
