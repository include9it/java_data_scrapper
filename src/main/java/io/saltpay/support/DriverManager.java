package io.saltpay.support;

import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {
    private final Driver driver = Driver.getInstance();

    public ChromeDriver createChromeDriver() {
        driver.setDriver();

        return driver.getDriver();
    }
}
