package io.saltpay.robot;

import io.saltpay.models.SsnData;
import io.saltpay.scrapper.CreditInfoScrapper;
import io.saltpay.storage.CreditInfoStorage;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_BACKUP_FILE;

public class CreditInfoDataCollector {
    private static final String TAG = CreditInfoDataCollector.class.getName();

    private final CreditInfoScrapper creditInfoScrapper;
    private final List<String> listOfSsn;
    private final CreditInfoStorage ciSaveManager;
    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfoDataCollector(CreditInfoScrapper creditInfoScrapper, List<String> listOfSsn, CreditInfoStorage ciSaveManager) {
        this.creditInfoScrapper = creditInfoScrapper;
        this.listOfSsn = listOfSsn;
        this.ciSaveManager = ciSaveManager;
    }

    public void start() {
        SaltLogger.i(TAG, "CreditInfo Bot started!");

        creditInfoScrapper.enterAndLogin();
        creditInfoScrapper.changeLocale();

        // Get Procurators data by SSN number
        listOfSsn.forEach(ssn -> {
            SsnData ssnData = creditInfoScrapper.findAndCollectDataBySsn(ssn);

            ciSaveManager.saveSsnData(CREDIT_INFO_BACKUP_FILE, ssnData);

            listOfSsnData.add(ssnData);
        });

        creditInfoScrapper.finish();
    }

    public List<SsnData> getListOfSsnData() {
        return listOfSsnData;
    }
}
