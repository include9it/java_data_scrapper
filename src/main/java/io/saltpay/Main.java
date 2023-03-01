package io.saltpay;

import io.saltpay.model.SsnData;
import io.saltpay.scrapper.CreditInfoDataCollector;
import io.saltpay.scrapper.CreditInfoScrapper;
import io.saltpay.support.DriverManager;
import io.saltpay.threads.CreditInfoDataCollectorThreadManager;
import io.saltpay.utils.*;

import java.io.IOException;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class Main {

    public static void main(String[] args) throws IOException {
        DriverManager driverManager = new DriverManager();

        CreditInfoSaveManager ciSaveManager = new CreditInfoSaveManager();
        CreditInfoSsnManager creditInfoSsnManager = new CreditInfoSsnManager();


        // CreditInfo Island company registry
//        basicCollectCreditInfo(ciSaveManager, creditInfoSsnManager, driverManager);
//        multiThreadCollectCreditInfo(ciSaveManager, creditInfoSsnManager, driverManager);


        // Phone number registry
//        basicCollectPhoneNumbers();
    }

    private static void basicCollectCreditInfo(
            CreditInfoSaveManager ciSaveManager,
            CreditInfoSsnManager creditInfoSsnManager,
            DriverManager driverManager
    ) throws IOException {
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

    private static void multiThreadCollectCreditInfo(
            CreditInfoSaveManager ciSaveManager,
            CreditInfoSsnManager creditInfoSsnManager,
            DriverManager driverManager
    ) throws IOException {
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

    private static void basicCollectPhoneNumbers() {
//        JaPhoneNumber jaPhoneNumber = new JaPhoneNumber(stepsManager);
//        jaPhoneNumber.start();
    }
}