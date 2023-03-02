package io.saltpay.storage;

import io.saltpay.utils.SaltLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.DATA_MODEL_FILE_EXTENSION;
import static io.saltpay.utils.Constants.RESOURCE_FILE_PATH;

public class FileManager {

    public static List<Object> readObjectsFromFile(String fileName) {
        return (List<Object>) readObjectFromFile(fileName);
    }

    public static <T> void writeObjectsToFile(String fileName, List<T> objects) {
        writeObjectToFile(fileName, objects);
    }

    public static <T> void appendObjectsToFile(String fileName, List<T> objects) {
        List<Object> objectList = (List<Object>) readObjectFromFile(fileName);

        if (objectList == null) {
            objectList = new ArrayList<>();
        } else {
            objectList.addAll(objects);
        }

        writeObjectToFile(fileName, objectList);
    }

    private static void writeObjectToFile(String fileName, Object object) {
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

    private static Object readObjectFromFile(String fileName) {
        Object object;

        try {
            FileInputStream fileIn = new FileInputStream(getFilePathWithExtension(fileName));
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            object = objectIn.readObject();

            objectIn.close();
            fileIn.close();

            System.out.println("Objects read from file successfully.");
        } catch (IOException | ClassNotFoundException e) {
            SaltLogger.basic("File not found -> " + fileName);
            SaltLogger.basic("By path -> " + getFilePathWithExtension(fileName));

            return null;
        }

        return object;
    }

    private static String getFilePathWithExtension(String fileName) {
        return RESOURCE_FILE_PATH + fileName + DATA_MODEL_FILE_EXTENSION;
    }
}
