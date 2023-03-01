package io.saltpay.utils;

import io.saltpay.model.SsnData;

import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_DATA_THREAD_FILE;
import static io.saltpay.utils.Constants.CREDIT_INFO_DATA_FILE;

public class CreditInfoSaveManager {

    private Boolean hasSavedData = null;
    private Boolean hasSavedThreadData = null;

    public void saveSsnData(SsnData ssnData) {
        String fileName = CREDIT_INFO_DATA_FILE;

        if (checkHasSavedData(fileName)) {
            FileManager.appendModelToFile(fileName, ssnData);

            return;
        }

        FileManager.writeModelToFile(fileName, ssnData);

        hasSavedData = true;
    }

    public List<SsnData> readSavedSsnData() {
        String fileName = CREDIT_INFO_DATA_FILE;

        if (checkHasSavedData(fileName)) {
            return FileManager.readModelsFromFile(fileName);
        }

        return null;
    }

    private boolean checkHasSavedData(String fileName) {
        if (hasSavedData == null) {
            hasSavedData = FileManager.readModelsFromFile(fileName) != null;
        }

        return hasSavedData;
    }

    //
    //
    ///
    // Backup file IO logic for threads test

    public void saveSsnThreadData(List<SsnData> ssnDataList) {
        String fileName = CREDIT_INFO_DATA_THREAD_FILE;

        if (checkHasSavedThreadData(fileName)) {
            FileManager.appendModelToFile(fileName, ssnDataList);

            return;
        }

        FileManager.writeModelToFile(fileName, ssnDataList);

        hasSavedThreadData = true;
    }

    public List<SsnData> readSavedThreadSsnData() {
        String fileName = CREDIT_INFO_DATA_THREAD_FILE;

        if (checkHasSavedThreadData(fileName)) {
            return FileManager.readModelsFromFile(fileName);
        }

        return null;
    }

    private boolean checkHasSavedThreadData(String fileName) {
        if (hasSavedThreadData == null) {
            hasSavedThreadData = FileManager.readModelsFromFile(fileName) != null;
        }

        return hasSavedThreadData;
    }
}
