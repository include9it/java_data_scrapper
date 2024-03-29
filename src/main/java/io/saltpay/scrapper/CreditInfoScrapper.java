package io.saltpay.scrapper;

import io.saltpay.models.Procurator;
import io.saltpay.models.SsnData;
import io.saltpay.steps.StepController;
import io.saltpay.support.Driver;
import io.saltpay.utils.DataCollectUtil;
import io.saltpay.utils.SaltLogger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class CreditInfoScrapper {
    private static final String TAG = CreditInfoScrapper.class.getName();

    private final ChromeDriver chromeDriver;
    private final StepController stepController;

    public CreditInfoScrapper(Driver driver) {
        this.chromeDriver = driver.setupChromeDriver();
        this.stepController = new StepController(chromeDriver);
    }

    public void enterAndLogin() {
        stepController.getLoginSteps().startPage(CREDIT_INFO_LINK);
        stepController.getLoginSteps().enterCredentials(
                "usernameInput",
                "Password",
                "",
                ""
        );
        stepController.getLoginSteps().login("audkenni-button");
    }

    public void changeLocale() {
        stepController.getActionSteps().changeLanguage();
    }

    public SsnData findAndCollectDataBySsn(String ssnValue) {
        List<Procurator> listOfProcurator;

        try {
            listOfProcurator = findAndCollectProcuratorBySsn(ssnValue);
        } catch (NoSuchElementException e) {
            SaltLogger.e(TAG, "Procurator data doesn't exist! Writing empty space");

            listOfProcurator = new ArrayList<>();
            listOfProcurator.add(new Procurator("Null", "Null"));
        } catch (TimeoutException e) {
            SaltLogger.e(TAG, "Can't find Procurator data! Writing empty space");

            listOfProcurator = new ArrayList<>();
            listOfProcurator.add(new Procurator("Null", "Null"));
        }

        return new SsnData(ssnValue, listOfProcurator);
    }

    public void finish() {
        chromeDriver.quit();
    }

    private List<Procurator> findAndCollectProcuratorBySsn(String ssnValue) throws NoSuchElementException, TimeoutException {
        stepController.getNavigationSteps().enterRegistryOfCompanies(ssnValue);

        return findAndCollectProcuratorData();
    }

    private List<Procurator> findAndCollectProcuratorData() throws NoSuchElementException, TimeoutException {
        List<List<WebElement>> listOfProcuratorData = extractProcuratorData();

        return DataCollectUtil.collectProcuratorsData(listOfProcuratorData);
    }

    private List<List<WebElement>> extractProcuratorData() throws NoSuchElementException, TimeoutException {
        List<WebElement> procuratorRows = stepController.getPageSearchSteps().findProcuratorRows();

        return stepController.getDataSearchSteps().findProcuratorFirstCellData(procuratorRows);
    }
}
