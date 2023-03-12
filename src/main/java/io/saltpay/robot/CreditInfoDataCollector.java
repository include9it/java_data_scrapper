package io.saltpay.robot;

import io.saltpay.models.SsnData;
import io.saltpay.scrapper.CreditInfoScrapper;
import io.saltpay.storage.StorageController;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

public class CreditInfoDataCollector {
    private static final String TAG = CreditInfoDataCollector.class.getName();

    private final CreditInfoScrapper creditInfoScrapper;
    private final List<String> listOfSsn;
    private final StorageController storage;
    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfoDataCollector(CreditInfoScrapper creditInfoScrapper, List<String> listOfSsn, StorageController storage) {
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
