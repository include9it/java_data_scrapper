package io.saltpay.scrapper;

import io.saltpay.models.SsnData;
import io.saltpay.storage.FileStorageController;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

public class CreditInfoScriptWrapper {
    private static final String TAG = CreditInfoScriptWrapper.class.getName();

    private final CreditInfoScrapper creditInfoScrapper;
    private final List<String> listOfSsn;
    private final FileStorageController storage;
    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfoScriptWrapper(CreditInfoScrapper creditInfoScrapper, List<String> listOfSsn, FileStorageController storage) {
        this.creditInfoScrapper = creditInfoScrapper;
        this.listOfSsn = listOfSsn;
        this.storage = storage;
    }

    public void start() {
        SaltLogger.i(TAG, "CreditInfo Bot started!");

        creditInfoScrapper.enterAndLogin();
        creditInfoScrapper.changeLocale();

        // Get Procurators data by SSN number
        listOfSsn.forEach(ssn -> {
            SsnData ssnData = creditInfoScrapper.findAndCollectDataBySsn(ssn);

            storage.saveData(ssnData);

            listOfSsnData.add(ssnData);
        });

        creditInfoScrapper.finish();
    }

    public List<SsnData> getListOfSsnData() {
        return listOfSsnData;
    }
}
