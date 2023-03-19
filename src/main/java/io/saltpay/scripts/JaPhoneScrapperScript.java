package io.saltpay.scripts;

import io.saltpay.models.PhoneNumbers;
import io.saltpay.models.ProcuratorPhones;
import io.saltpay.support.Driver;
import io.saltpay.utils.DataCollectUtil;
import io.saltpay.utils.SaltLogger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;

public class JaPhoneScrapperScript extends Script {
    private static final String TAG = JaPhoneScrapperScript.class.getName();

    public JaPhoneScrapperScript(Driver driver) {
        super(driver);
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
