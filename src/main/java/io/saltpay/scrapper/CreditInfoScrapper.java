package io.saltpay.scrapper;

import io.saltpay.model.Procurator;
import io.saltpay.model.SsnData;
import io.saltpay.steps.StepController;
import io.saltpay.support.DriverManager;
import io.saltpay.utils.DataCollectUtil;
import io.saltpay.utils.SaltLogger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class CreditInfoScrapper {
    private static final String TAG = CreditInfoScrapper.class.getName();

    private final StepController stepController;

    public CreditInfoScrapper(DriverManager driverManager) {
        this.stepController = new StepController(driverManager.createChromeDriver());
    }

    public void enterAndLogin() {
        stepController.getLoginSteps().startPage(CREDIT_INFO_LINK);
        stepController.getLoginSteps().enterCredentials(
                "usernameInput",
                "Password",
                "Salt.Elisabet",
                "Elisabet_69"
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
