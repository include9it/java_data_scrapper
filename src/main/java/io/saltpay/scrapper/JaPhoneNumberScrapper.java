package io.saltpay.scrapper;

import io.saltpay.model.PhoneNumbers;
import io.saltpay.model.ProcuratorPhones;
import io.saltpay.steps.StepController;
import io.saltpay.support.DriverManager;
import io.saltpay.utils.DataCollectUtil;
import io.saltpay.utils.SaltLogger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static io.saltpay.utils.Constants.JA_PHONE_REGISTRY_LINK;

public class JaPhoneNumberScrapper {
    private static final String TAG = JaPhoneNumberScrapper.class.getName();

    private final StepController stepController;

    public JaPhoneNumberScrapper(DriverManager driverManager) {
        this.stepController = new StepController(driverManager.createChromeDriver());
    }

    public void enterWebsite() {
        stepController.getLoginSteps().startPage(JA_PHONE_REGISTRY_LINK);
    }

    public ProcuratorPhones findAndCollectDataByFullName(String fullName) {
        PhoneNumbers phoneNumbers;

        try {
            phoneNumbers = findAndCollectProcuratorBySsn(fullName);
        } catch (NoSuchElementException e) {
            SaltLogger.e(TAG, "Procurator phone numbers doesn't exist! Writing empty space");

            phoneNumbers = new PhoneNumbers("Null", "Null");
        } catch (TimeoutException e) {
            SaltLogger.e(TAG, "Can't find Procurator phone numbers! Writing empty space");

            phoneNumbers = new PhoneNumbers("Null", "Null");
        }

        return new ProcuratorPhones(fullName, phoneNumbers);
    }

    private PhoneNumbers findAndCollectProcuratorBySsn(String fullName) throws NoSuchElementException, TimeoutException {
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
