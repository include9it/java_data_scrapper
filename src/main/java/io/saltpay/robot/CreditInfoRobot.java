package io.saltpay.robot;

import io.saltpay.models.SsnData;
import io.saltpay.scrapper.CreditInfoScrapper;
import io.saltpay.storage.StorageController;
import io.saltpay.support.Driver;
import io.saltpay.storage.CreditInfoSsnManager;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_BACKUP_FILE;
import static io.saltpay.utils.Constants.THREADS;

public class CreditInfoRobot {
    private final Driver driver;
    private final StorageController storage = new StorageController(CREDIT_INFO_BACKUP_FILE);
    private final CreditInfoSsnManager creditInfoSsnManager = new CreditInfoSsnManager();

    public CreditInfoRobot(Driver driver) {
        this.driver = driver;
    }

    public void basicCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData(storage);

        // Start data collection process
        CreditInfoScrapper creditInfoScrapper = new CreditInfoScrapper(driver);
        CreditInfoDataCollector creditInfoDataCollector = new CreditInfoDataCollector(
                creditInfoScrapper,
                listOfSsn,
                storage
        );
        // 15 requests per 1 min // For 4492 records will be approximately 4 hours, 59 minutes
//        creditInfoDataCollector.start();

        // Prepare Excel file
        List<SsnData> savedSsnData = storage.readData();
        SaltLogger.basic("savedSsnData size: " + savedSsnData.size());
//        creditInfoSsnManager.prepareExcelWithSsnData(savedSsnData);
    }

    public void multiThreadCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData(storage);

        // Start multi thread collecting info process
        List<SsnData> multiThreadSsnDataList = CreditInfoThreadBot.start(THREADS, listOfSsn, driver);
        storage.saveData(multiThreadSsnDataList);

        // Prepare Excel file
        List<SsnData> savedThreadSsnData = storage.readData();
        SaltLogger.basic("savedSsnThreadData size: " + savedThreadSsnData.size());
        creditInfoSsnManager.prepareExcelWithSsnData(savedThreadSsnData);
    }
}
