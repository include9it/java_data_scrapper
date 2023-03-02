package io.saltpay.storage;

import io.saltpay.utils.SaltLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.DATA_MODEL_FILE_EXTENSION;
import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class FileManager {

    public static void appendModelToFile(String fileName, Object model) {
        List<Object> lisOfObjects = readModelsFromFile(fileName);

        if (lisOfObjects == null) {
            lisOfObjects = new ArrayList<>();
        } else {
            lisOfObjects.add(model);
        }

        writeModelsToFile(fileName, lisOfObjects);
    }

    public static void appendModelToFile(String fileName, List<Object> objectList) {
        List<Object> lisOfObjects = readModelsFromFile(fileName);

        if (lisOfObjects == null) {
            lisOfObjects = new ArrayList<>();
        } else {
            lisOfObjects.addAll(objectList);
        }

        writeModelsToFile(fileName, lisOfObjects);
    }

    public static void writeModelToFile(String fileName, Object model) {
        List<Object> lisOfObjects = new ArrayList<>();
        lisOfObjects.add(model);

        writeModelsToFile(fileName, lisOfObjects);
    }

    public static void writeModelToFile(String fileName, List<Object> lisOfObjects) {
        writeModelsToFile(fileName, lisOfObjects);
    }

    public static void writeModelsToFile(String fileName, List<Object> models) {
        try {
            FileOutputStream fileOut = new FileOutputStream(RESOURCE_FILE_PATH + fileName + DATA_MODEL_FILE_EXTENSION);
            ObjectOutputStream objOut;
            objOut = new ObjectOutputStream(fileOut);

            objOut.writeObject(models);

            objOut.close();
            fileOut.close();

            SaltLogger.basic("Model saved to " + fileName + DATA_MODEL_FILE_EXTENSION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> readModelsFromFile(String fileName) {
        List<Object> models;

        try {
            ObjectInputStream objIn = new ObjectInputStream(
                    new FileInputStream(RESOURCE_FILE_PATH + fileName + DATA_MODEL_FILE_EXTENSION)
            );

            models = (List<Object>) objIn.readObject();

            objIn.close();

            SaltLogger.basic("Model loaded from " + fileName + DATA_MODEL_FILE_EXTENSION);
        } catch (IOException | ClassNotFoundException e) {
            SaltLogger.basic("Model not found -> " + fileName + DATA_MODEL_FILE_EXTENSION);

            return null;
        }

        return models;
    }
}
