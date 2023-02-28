package io.saltpay.utils;

import io.saltpay.model.SsnData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.DATA_MODEL_FILE_EXTENSION;
import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class FileManager {

    public static void appendModelToFile(String fileName, SsnData model) {
        List<SsnData> lisOfSsnData = readModelsFromFile(fileName);
        lisOfSsnData.add(model);

        writeModelsToFile(fileName, lisOfSsnData);
    }

    public static void writeModelToFile(String fileName, SsnData model) {
        List<SsnData> lisOfSsnData = new ArrayList<>();
        lisOfSsnData.add(model);

        writeModelsToFile(fileName, lisOfSsnData);
    }

    public static void writeModelsToFile(String fileName, List<SsnData> models) {
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

    public static List<SsnData> readModelsFromFile(String fileName) {
        List<SsnData> models;

        try {
            ObjectInputStream objIn = new ObjectInputStream(
                    new FileInputStream(RESOURCE_FILE_PATH + fileName + DATA_MODEL_FILE_EXTENSION)
            );

            models = (List<SsnData>) objIn.readObject();

            objIn.close();

            SaltLogger.basic("Model loaded from " + fileName + DATA_MODEL_FILE_EXTENSION);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            return null;
        }

        return models;
    }
}
