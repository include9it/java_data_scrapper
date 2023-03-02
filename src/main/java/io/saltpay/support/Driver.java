package io.saltpay.support;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Driver {

    private ChromeDriver driver;

    public void setDriver() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless"); // This option allow to browse without opening browser

        this.driver = new ChromeDriver(options);
    }

    public ChromeDriver getDriver() {
        return this.driver;
    }
}