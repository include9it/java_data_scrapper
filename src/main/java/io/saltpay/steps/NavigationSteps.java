package io.saltpay.steps;

import io.saltpay.utils.SaltLogger;
import io.saltpay.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static io.saltpay.utils.Constants.CREDIT_INFO_COMPANY_REGISTRY;
import static io.saltpay.utils.Constants.CREDIT_INFO_LINK;

public class NavigationSteps {
    private static final String TAG = NavigationSteps.class.getName();

    private final ChromeDriver chromeDriver;

    public NavigationSteps(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
    }

    public void enterRegistryOfCompanies(String ssnValue) {
        String registryUrl = CREDIT_INFO_LINK + String.format(CREDIT_INFO_COMPANY_REGISTRY, ssnValue);

        chromeDriver.get(registryUrl);

        WaitUtil.wait(ExpectedConditions.urlContains(registryUrl));
        WaitUtil.waitLong(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        SaltLogger.i(TAG, "Entered: Registry of companies by SSN: " + ssnValue);
    }
}
