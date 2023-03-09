package io.saltpay.storage;

import io.saltpay.utils.SaltLogger;

import java.util.Collections;
import java.util.List;

public class StorageController extends Storage {
    private boolean hasSavedData = false;

    public <T> void saveData(String absoluteFileName, T data) {
        if (hasSavedData) {
            appendObjectsToFile(absoluteFileName, Collections.singletonList(data));

            return;
        }

        writeToFile(absoluteFileName, data);

        hasSavedData = true;
    }

    public <T> T readData(String absoluteFileName) {
        if (hasSavedData) {
            return readFromFile(absoluteFileName);
        }

        return null;
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
