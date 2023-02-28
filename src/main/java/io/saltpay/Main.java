package io.saltpay;

import io.saltpay.scrapper.CreditInfo;
import io.saltpay.scrapper.JaPhoneNumber;
import io.saltpay.steps.StepsManager;
import io.saltpay.support.Driver;
import io.saltpay.threads.ScrapperTask;
import io.saltpay.threads.Task;
import io.saltpay.threads.ThreadManager;
import io.saltpay.utils.CreditInfoSaveManager;
import io.saltpay.utils.FileManager;
import org.openqa.selenium.chrome.ChromeDriver;

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

        CreditInfo creditInfoScrapper = new CreditInfo(stepsManager, ciSaveManager); // 15 requests per 1 min // Approximately 4 hours, 59 minutes
        creditInfoScrapper.start();

//        JaPhoneNumber jaPhoneNumber = new JaPhoneNumber(stepsManager);
//        jaPhoneNumber.start();

        // Start multi threading info scrap process
        List<Task> listOfTask = new ArrayList<>();
        listOfTask.add(new ScrapperTask(creditInfoScrapper));
        ThreadManager.start(10, listOfTask);
    }
}