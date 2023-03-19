package io.saltpay.storage;

import io.saltpay.utils.SaltLogger;

import java.io.*;

class FileStorage {
    private final String absoluteFileName;

    FileStorage(String absoluteFileName) {
        this.absoluteFileName = absoluteFileName;
    }

    <T> void writeToFile(T storageData) {
        try {
            FileOutputStream fileOut = new FileOutputStream(absoluteFileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(storageData);

            objectOut.close();
            fileOut.close();

            SaltLogger.basic("File written successfully.");
        } catch (IOException e) {
            SaltLogger.basic("Can't write file.");

            e.printStackTrace();
        }
    }

    <T> T readFromFile() {
        Object storageData;

        try {
            FileInputStream fileIn = new FileInputStream(absoluteFileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            storageData = objectIn.readObject();

            objectIn.close();
            fileIn.close();

            SaltLogger.basic("File read successfully.");
        } catch (IOException | ClassNotFoundException e) {
            SaltLogger.basic("File not found -> " + absoluteFileName);

            e.printStackTrace();

            return null;
        }

        return (T) storageData;
    }
}
