package io.saltpay;

import io.saltpay.scrapper.CreditInfo;
import io.saltpay.scrapper.JaPhoneNumber;
import io.saltpay.steps.StepsManager;
import io.saltpay.support.Driver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // Setup driver
        Driver driver = Driver.getInstance();
        driver.setDriver();

        StepsManager stepsManager = new StepsManager(driver.getDriver());

        CreditInfo creditInfoScrapper = new CreditInfo(stepsManager); // 15 requests per 1 min // Approximately 4 hours, 59 minutes
        creditInfoScrapper.start();

//        JaPhoneNumber jaPhoneNumber = new JaPhoneNumber(stepsManager);
//        jaPhoneNumber.start();
    }
}