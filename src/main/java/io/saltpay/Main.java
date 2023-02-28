package io.saltpay;

import io.saltpay.scrapper.CreditInfo;
import io.saltpay.steps.StepsManager;
import io.saltpay.support.Driver;
import io.saltpay.threads.ScrapperTask;
import io.saltpay.threads.Task;
import io.saltpay.threads.ThreadManager;
import io.saltpay.utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        // Setup driver
        Driver driver = Driver.getInstance();
        driver.setDriver();

        StepsManager stepsManager = new StepsManager(driver.getDriver());
        CreditInfoSaveManager ciSaveManager = new CreditInfoSaveManager();

        CreditInfoSsnManager creditInfoSsnManager = new CreditInfoSsnManager();

        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData(ciSaveManager);

        CreditInfo creditInfoScrapper = new CreditInfo(listOfSsn, stepsManager, ciSaveManager); // 15 requests per 1 min // Approximately 4 hours, 59 minutes
        creditInfoScrapper.start();

        creditInfoSsnManager.prepareExcelWithSsnData(ciSaveManager.readSavedSsnData());


//        JaPhoneNumber jaPhoneNumber = new JaPhoneNumber(stepsManager);
//        jaPhoneNumber.start();




        // Start multi thread collecting info process
//        List<Task> listOfTask = new ArrayList<>();
//        listOfTask.add(new ScrapperTask(creditInfoScrapper));
//        ThreadManager.start(10, listOfTask);
//
//        creditInfoSsnManager.prepareExcelWithSsnData(creditInfoScrapper.getListOfSsnData());
    }
}