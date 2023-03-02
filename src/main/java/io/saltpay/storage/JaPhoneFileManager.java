package io.saltpay.storage;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.utils.SaltLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.saltpay.utils.Constants.DATA_MODEL_FILE_EXTENSION;
import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class JaPhoneFileManager {

    public static List<ProcuratorPhones> readObjectsFromFile(String fileName) {
        List<Object> objectList = readObjectFromFile(fileName);

        if (objectList == null) {
            return null;
        }

        return objectList.stream()
                .map(object -> (ProcuratorPhones) object)
                .collect(Collectors.toList());
    }

    public static void writeObjectsToFile(String fileName, List<ProcuratorPhones> objects) {
        writeObjectToFile(fileName, objects);
    }

    public static void appendObjectsToFile(String fileName, List<ProcuratorPhones> objects) {
        List<ProcuratorPhones> objectList = readObjectsFromFile(fileName);

        if (objectList == null) {
            objectList = new ArrayList<>();
        } else {
            objectList.addAll(objects);
        }

        writeObjectToFile(fileName, objectList);
    }

    private static void writeObjectToFile(String fileName, List<ProcuratorPhones> object) {
        try {
            FileOutputStream fileOut = new FileOutputStream(getFilePathWithExtension(fileName));
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(object);

            objectOut.close();
            fileOut.close();

            System.out.println("Objects written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Object> readObjectFromFile(String fileName) {
        List<Object> objects;

        try {
            FileInputStream fileIn = new FileInputStream(getFilePathWithExtension(fileName));
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            objects = (List<Object>) objectIn.readObject();

            objectIn.close();
            fileIn.close();

            System.out.println("Objects read from file successfully.");
        } catch (IOException | ClassNotFoundException e) {
            SaltLogger.basic("File not found -> " + fileName);
            SaltLogger.basic("By path -> " + getFilePathWithExtension(fileName));

            return null;
        }

        return objects;
    }

    private static String getFilePathWithExtension(String fileName) {
        return RESOURCE_FILE_PATH + fileName + DATA_MODEL_FILE_EXTENSION;
    }
}
