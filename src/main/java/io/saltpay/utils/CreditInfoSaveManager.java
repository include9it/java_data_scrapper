package io.saltpay.utils;

import io.saltpay.model.SsnData;

import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_DATA_FILE;

public class CreditInfoSaveManager {
    public void saveSsnData(SsnData ssnData) {
        if (hasSavedData()) {
            FileManager.appendModelToFile(CREDIT_INFO_DATA_FILE, ssnData);

            return;
        }

        FileManager.writeModelToFile(CREDIT_INFO_DATA_FILE, ssnData);
    }

    public List<SsnData> readSavedSsnData() {
        if (hasSavedData()) {
            return FileManager.readModelsFromFile(CREDIT_INFO_DATA_FILE);
        }

        return null;
    }

    private boolean hasSavedData() {
        return FileManager.readModelsFromFile(CREDIT_INFO_DATA_FILE) != null;
    }
}
