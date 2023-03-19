package io.saltpay.scrapper;

import io.saltpay.models.PhoneNumbers;
import io.saltpay.models.ProcuratorPhones;
import io.saltpay.steps.StepController;
import io.saltpay.support.Driver;
import io.saltpay.utils.DataCollectUtil;
import io.saltpay.utils.SaltLogger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static io.saltpay.utils.Constants.JA_PHONE_REGISTRY_LINK;

public class JaPhoneNumberScrapperScript {
    private static final String TAG = JaPhoneNumberScrapperScript.class.getName();

    private final ChromeDriver chromeDriver;
    private final StepController stepController;

    public JaPhoneNumberScrapperScript(Driver driver) {
        this.chromeDriver = driver.setupChromeDriver();
        this.stepController = new StepController(chromeDriver);
    }

    public void enterWebsite() {
        stepController.getLoginSteps().startPage(JA_PHONE_REGISTRY_LINK);
    }

    public ProcuratorPhones findAndCollectDataByFullName(String fullName) {
        SaltLogger.i(TAG, "Collecting phone number data for " + fullName);

        PhoneNumbers phoneNumbers;

        try {
            phoneNumbers = findAndCollectPhonesByProcurator(fullName);
        } catch (NoSuchElementException e) {
            SaltLogger.e(TAG, "Info for person: " + fullName);
            SaltLogger.e(TAG, "Procurator phone numbers doesn't exist! Writing empty space");

            phoneNumbers = new PhoneNumbers("", "");
        } catch (TimeoutException | StaleElementReferenceException e) {
            SaltLogger.e(TAG, "Can't find Procurator phone numbers! Writing empty space");

            phoneNumbers = new PhoneNumbers("", "");
        }

        return new ProcuratorPhones(fullName, phoneNumbers);
    }

    public void finish() {
        chromeDriver.quit();
    }

    private PhoneNumbers findAndCollectPhonesByProcurator(String fullName) throws NoSuchElementException, TimeoutException, StaleElementReferenceException {
        stepController.getNavigationSteps().enterPhoneInfoByProcurator(fullName);

        return findAndCollectProcuratorData();
    }

    private PhoneNumbers findAndCollectProcuratorData() throws NoSuchElementException, TimeoutException {
        List<WebElement> listOfProcuratorPhones = extractProcuratorData();

        return DataCollectUtil.collectProcuratorPhones(listOfProcuratorPhones);
    }

    private List<WebElement> extractProcuratorData() throws NoSuchElementException, TimeoutException {
        List<WebElement> procuratorPhones = stepController.getPageSearchSteps().findProcuratorPhones();

        return stepController.getDataSearchSteps().findProcuratorPhonesData(procuratorPhones);
    }
}
