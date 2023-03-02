package io.saltpay.robot;

import io.saltpay.model.SsnData;
import io.saltpay.scrapper.CreditInfoScrapper;
import io.saltpay.support.DriverManager;
import io.saltpay.threads.CreditInfoDataCollectorThreadManager;
import io.saltpay.utils.CreditInfoSaveManager;
import io.saltpay.utils.CreditInfoSsnManager;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_BACKUP_FILE;

public class CreditInfoRobot {
    private final DriverManager driverManager;
    private final CreditInfoSaveManager ciSaveManager = new CreditInfoSaveManager();
    private final CreditInfoSsnManager creditInfoSsnManager = new CreditInfoSsnManager();

    public CreditInfoRobot(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    public void basicCollectCreditInfo() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData(ciSaveManager);

        // Start data collection process
        CreditInfoScrapper creditInfoScrapper = new CreditInfoScrapper(driverManager);
        CreditInfoDataCollector creditInfoDataCollector = new CreditInfoDataCollector(
                creditInfoScrapper,
                listOfSsn,
                ciSaveManager
        );
        // 15 requests per 1 min // For 4492 records will be approximately 4 hours, 59 minutes
        creditInfoDataCollector.start();

        // Prepare Excel file
        List<SsnData> savedSsnData = ciSaveManager.readSavedSsnData(CREDIT_INFO_BACKUP_FILE);
        SaltLogger.basic("savedSsnData size: " + savedSsnData.size());
        creditInfoSsnManager.prepareExcelWithSsnData(savedSsnData);
    }

    public void multiThreadCollectCreditInfo() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData(ciSaveManager);

        // Start multi thread collecting info process
        List<SsnData> multiThreadSsnDataList = CreditInfoDataCollectorThreadManager.start(10, listOfSsn, driverManager);
        ciSaveManager.saveSsnData(CREDIT_INFO_BACKUP_FILE, multiThreadSsnDataList);

        // Prepare Excel file
        List<SsnData> savedThreadSsnData = ciSaveManager.readSavedSsnData(CREDIT_INFO_BACKUP_FILE);
        SaltLogger.basic("savedSsnThreadData size: " + savedThreadSsnData.size());
        creditInfoSsnManager.prepareExcelWithSsnData(savedThreadSsnData);
    }
}
