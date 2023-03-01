package io.saltpay.steps;

import io.saltpay.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class PageSearchSteps {
    private static final String TAG = PageSearchSteps.class.getName();

    private final ChromeDriver chromeDriver;

    public PageSearchSteps(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
    }

    public List<WebElement> findProcuratorRows() throws NoSuchElementException, TimeoutException {
        return findSpecificRows("Managing director, procuration, auditors and founders", "Procurator");
    }

    public List<WebElement> findUboRows() {
        return findRows("Ultimate beneficial owners");
    }

    private List<WebElement> findRows(String rowName) {
        WebElement targetTable = findTable(rowName);

        return targetTable.findElements(By.xpath(".//tr"));
    }

    private List<WebElement> findSpecificRows(String rowName, String tag) throws NoSuchElementException, TimeoutException {
        WebElement targetTable = findTable(rowName);

        // Assuming that the table is already located in a WebElement object called "tableElement"
        return targetTable.findElements(By.xpath(".//tr[contains(., '" + tag + "')]"));
    }

    private WebElement findTable(String rowName) throws NoSuchElementException, TimeoutException {
        String targetXpath = "//*[contains(text(),'" + rowName + "')]";

        WaitUtil.waitLong(ExpectedConditions.presenceOfElementLocated(By.xpath(targetXpath)), chromeDriver);

        WebElement targetTitle = chromeDriver.findElement(By.xpath(targetXpath));

        return targetTitle.findElement(By.xpath("following-sibling::div"));
    }
}
