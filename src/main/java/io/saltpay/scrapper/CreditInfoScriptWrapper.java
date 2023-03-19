package io.saltpay.scrapper;

import io.saltpay.models.SsnData;
import io.saltpay.storage.FileStorageController;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

public class CreditInfoScriptWrapper {
    private static final String TAG = CreditInfoScriptWrapper.class.getName();

    private final CreditInfoScrapperScript creditInfoScrapperScript;
    private final List<String> listOfSsn;
    private final FileStorageController storage;
    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfoScriptWrapper(CreditInfoScrapperScript creditInfoScrapperScript, List<String> listOfSsn, FileStorageController storage) {
        this.creditInfoScrapperScript = creditInfoScrapperScript;
        this.listOfSsn = listOfSsn;
        this.storage = storage;
    }

    public void start() {
        SaltLogger.i(TAG, "CreditInfo Bot started!");

        creditInfoScrapperScript.enterAndLogin();
        creditInfoScrapperScript.changeLocale();

        // Get Procurators data by SSN number
        listOfSsn.forEach(ssn -> {
            SsnData ssnData = creditInfoScrapperScript.findAndCollectDataBySsn(ssn);

            storage.saveData(ssnData);

            listOfSsnData.add(ssnData);
        });

        creditInfoScrapperScript.finish();
    }

    public List<SsnData> getListOfSsnData() {
        return listOfSsnData;
    }
}
