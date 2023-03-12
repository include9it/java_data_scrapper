package io.saltpay.storage;

import io.saltpay.utils.SaltLogger;

import java.util.Collections;
import java.util.List;

public class StorageController extends Storage {
    private boolean hasSavedData = false;

    StorageController(String absoluteFileName) {
        super(absoluteFileName);

        checkFileExistence();
    }

    public <T> void saveData(T data) {
        saveData(Collections.singletonList(data));
    }

    public <T> void saveData(List<T> data) {
        SaltLogger.basic("Saving data to file...");

        if (hasSavedData) {
            SaltLogger.basic("Appending data to file...");

            appendObjectsToFile(data);

            return;
        }

        writeToFile(data);

        hasSavedData = true;
    }

    public <T> T readData() {
        SaltLogger.basic("Reading data from file...");

        if (hasSavedData) {
            return readFromFile();
        }

        SaltLogger.basic("Can't read file! File is empty.");

        return readFromFile();
    }

    private <T> void appendObjectsToFile(List<T> listOfObjects) {
        List<T> objects = readFromFile();

        if (objects != null) {
            objects.addAll(listOfObjects);

            writeToFile(objects);

            return;
        }

        SaltLogger.basic("Can't append to file! File is empty.");
    }

    private void checkFileExistence() {
        hasSavedData = readFromFile() != null;
    }
}
