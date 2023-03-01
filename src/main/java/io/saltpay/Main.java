package io.saltpay;

import io.saltpay.model.SsnData;
import io.saltpay.scrapper.CreditInfoDataCollector;
import io.saltpay.scrapper.CreditInfoScrapper;
import io.saltpay.support.DriverManager;
import io.saltpay.threads.CreditInfoDataCollectorThreadManager;
import io.saltpay.utils.*;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        DriverManager driverManager = new DriverManager();

//        basicCollectCreditInfo(driverManager);

//        basicCollectPhoneNumbers();

        multiThreadCollectCreditInfo(driverManager);
    }

    private static void basicCollectCreditInfo(DriverManager driverManager) throws IOException {
        // Prepare list of input SSN numbers for data collection
        CreditInfoSaveManager ciSaveManager = new CreditInfoSaveManager();
        CreditInfoSsnManager creditInfoSsnManager = new CreditInfoSsnManager();
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
        List<SsnData> savedSsnData = ciSaveManager.readSavedSsnData();
//        List<SsnData> savedSsnChunk = savedSsnData.subList(1743, 3071);
        SaltLogger.basic("savedSsnData size: " + savedSsnData.size());
        creditInfoSsnManager.prepareExcelWithSsnData(savedSsnData);
    }

    private static void multiThreadCollectCreditInfo(DriverManager driverManager) throws IOException {
        // Prepare list of input SSN numbers for data collection
        CreditInfoSaveManager ciSaveManager = new CreditInfoSaveManager();
        CreditInfoSsnManager creditInfoSsnManager = new CreditInfoSsnManager();
        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData(ciSaveManager);

        // Start multi thread collecting info process
        List<SsnData> multiThreadSsnDataList = CreditInfoDataCollectorThreadManager.start(10, listOfSsn, driverManager);

        ciSaveManager.saveSsnThreadData(multiThreadSsnDataList);

        List<SsnData> savedThreadSsnData = ciSaveManager.readSavedThreadSsnData();

        SaltLogger.basic("savedSsnThreadData size: " + savedThreadSsnData.size());

        creditInfoSsnManager.prepareExcelWithSsnData(savedThreadSsnData);
    }

    private static void basicCollectPhoneNumbers() {
//        JaPhoneNumber jaPhoneNumber = new JaPhoneNumber(stepsManager);
//        jaPhoneNumber.start();
    }
}