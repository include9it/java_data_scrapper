package io.saltpay.support;

import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {

    public ChromeDriver createChromeDriver() {
        Driver driver = new Driver();

        driver.setDriver();

        return driver.getDriver();
    }
}
