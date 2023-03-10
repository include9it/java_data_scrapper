package io.saltpay.storage;

import io.saltpay.utils.SaltLogger;

import java.util.Collections;
import java.util.List;

public class StorageController extends Storage {
    private boolean hasSavedData = false;

    public <T> void saveData(String absoluteFileName, T data) {
        saveData(absoluteFileName, Collections.singletonList(data));
    }

    public <T> void saveData(String absoluteFileName, List<T> data) {
        SaltLogger.basic("Saving data to file...");

        if (hasSavedData) {
            SaltLogger.basic("Appending data to file...");

            appendObjectsToFile(absoluteFileName, data);

            return;
        }

        writeToFile(absoluteFileName, data);

        hasSavedData = true;
    }

    public <T> T readData(String absoluteFileName) {
        SaltLogger.basic("Reading data from file...");

        if (hasSavedData) {
            return readFromFile(absoluteFileName);
        }

        SaltLogger.basic("Can't read file! File is empty.");

        return readFromFile(absoluteFileName);
    }

    private <T> void appendObjectsToFile(String absoluteFileName, List<T> listOfObjects) {
        List<T> objects = readFromFile(absoluteFileName);

        if (objects != null) {
            objects.addAll(listOfObjects);

            writeToFile(absoluteFileName, objects);

            return;
        }

        SaltLogger.basic("Can't append to file! File is empty.");
    }
}
