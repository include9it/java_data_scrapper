package io.saltpay.steps;

import io.saltpay.utils.SaltLogger;
import io.saltpay.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

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

        WaitUtil.wait(ExpectedConditions.urlContains(registryUrl), chromeDriver);
        WaitUtil.waitLong(ExpectedConditions.presenceOfElementLocated(By.tagName("body")), chromeDriver);

        SaltLogger.i(TAG, "Entered: Registry of companies by SSN: " + ssnValue);
    }

    public void enterPhoneInfoByProcurator(String fullName) {
        WaitUtil.waitLong(ExpectedConditions.presenceOfElementLocated(By.tagName("body")), chromeDriver);

        WebElement searchBar = chromeDriver.findElement(By.className("search-bar"));

        WebElement input = searchBar.findElement(By.id("search-input"));
        input.clear();
        input.sendKeys(fullName);
        input.sendKeys(Keys.ENTER);

        WaitUtil.wait(ExpectedConditions.presenceOfElementLocated(By.className("results")), chromeDriver);

        WebElement results = chromeDriver.findElement(By.className("results"));

        WaitUtil.wait(ExpectedConditions.presenceOfElementLocated(By.className("search-result-list")), chromeDriver);

        List<WebElement> searchResultList = results.findElements(By.className("search-result-list"));

        if (searchResultList.size() != 1) {
            throw new NoSuchElementException("Element not found!");
        }

        List<WebElement> listElements = searchResultList.get(0).findElements(By.className("spaced-list-item"));

        if (listElements.size() != 1) {
            throw new NoSuchElementException("Element not found!");
        }

        List<WebElement> searchResultItem = searchResultList.get(0).findElements(By.className("search-result-list-item"));

        if (searchResultItem == null) {
            throw new NoSuchElementException("Element not found!");
        }

        searchResultItem.get(0).click();

        SaltLogger.i(TAG, "Entered: Phone info page of " + fullName);
    }
}
