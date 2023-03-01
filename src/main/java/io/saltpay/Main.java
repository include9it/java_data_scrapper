package io.saltpay;

import io.saltpay.model.SsnData;
import io.saltpay.scrapper.CreditInfo;
import io.saltpay.steps.StepController;
import io.saltpay.support.DriverManager;
import io.saltpay.utils.*;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        DriverManager driverManager = new DriverManager();

        StepController stepController = new StepController(driverManager.createChromeDriver());
        CreditInfoSaveManager ciSaveManager = new CreditInfoSaveManager();

        CreditInfoSsnManager creditInfoSsnManager = new CreditInfoSsnManager();

        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData(ciSaveManager);

        CreditInfo creditInfoScrapper = new CreditInfo(listOfSsn, stepController, ciSaveManager); // 15 requests per 1 min // Approximately 4 hours, 59 minutes
        creditInfoScrapper.start();

        List<SsnData> savedSsnData = ciSaveManager.readSavedSsnData();

//        List<SsnData> savedSsnChunk = savedSsnData.subList(1743, 3071);
        SaltLogger.basic("savedSsnData size: " + savedSsnData.size());

        creditInfoSsnManager.prepareExcelWithSsnData(savedSsnData);


//        JaPhoneNumber jaPhoneNumber = new JaPhoneNumber(stepsManager);
//        jaPhoneNumber.start();


//        // Start multi thread collecting info process
//        List<Task> listOfTasks = new ArrayList<>();
//        listOfTasks.add(new ScrapperTask(creditInfoScrapper));
//
//        ThreadManager.start(10, listOfTasks);

//        creditInfoSsnManager.prepareExcelWithSsnData(creditInfoScrapper.getListOfSsnData());
    }
}