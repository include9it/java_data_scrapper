package io.saltpay.scripts;

import io.saltpay.steps.StepController;
import io.saltpay.support.Driver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Script {

    private final ChromeDriver chromeDriver;
    protected final StepController stepController;

    public Script(Driver driver) {
        this.chromeDriver = driver.setupChromeDriver();

        this.stepController = new StepController(chromeDriver);
    }

    public void finish() {
        chromeDriver.quit();
    }
}
