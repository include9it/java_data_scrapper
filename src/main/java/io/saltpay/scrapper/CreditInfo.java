package io.saltpay.scrapper;

import io.saltpay.model.Procurator;
import io.saltpay.model.SsnData;
import io.saltpay.steps.StepsManager;
import io.saltpay.utils.CreditInfoSaveManager;
import io.saltpay.utils.DataCollectUtil;
import io.saltpay.utils.SaltLogger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class CreditInfo extends Scrapper {
    private static final String TAG = CreditInfo.class.getName();
    private final List<String> listOfSsn;
    private final CreditInfoSaveManager ciSaveManager;
    private final List<SsnData> listOfSsnData = new ArrayList<>();

    public CreditInfo(List<String> listOfSsn, StepsManager stepsManager, CreditInfoSaveManager ciSaveManager) {
        super(stepsManager);

        this.listOfSsn = listOfSsn;
        this.ciSaveManager = ciSaveManager;
    }

    @Override
    public void start() {
        enterAndLogin();
        changeLocale();

        // Get Procurators data by SSN number
        listOfSsn.forEach(ssn -> {
            SsnData ssnData = findAndCollectDataBySsn(ssn);

            ciSaveManager.saveSsnData(ssnData);

            listOfSsnData.add(ssnData);
        });
    }

    public List<SsnData> getListOfSsnData() {
        return listOfSsnData;
    }

    private void enterAndLogin() {
        getStepsManager().getLoginSteps().startPage(CREDIT_INFO_LINK);
        getStepsManager().getLoginSteps().enterCredentials(
                "usernameInput",
                "Password",
                "Salt.Elisabet",
                "Elisabet_69"
        );
        getStepsManager().getLoginSteps().login("audkenni-button");
    }

    private void changeLocale() {
        getStepsManager().getActionSteps().changeLanguage();
    }

    private SsnData findAndCollectDataBySsn(String ssnValue) {
        List<Procurator> listOfProcurator;

        try {
            listOfProcurator = findAndCollectProcuratorBySsn(ssnValue);
        } catch (NoSuchElementException e) {
            SaltLogger.e(TAG, "Procurator data doesn't exist! Writing empty space");

            listOfProcurator = new ArrayList<>();
            listOfProcurator.add(new Procurator("No Procurator data for:", ssnValue));
        } catch (TimeoutException e) {
            SaltLogger.e(TAG, "Can't find Procurator data! Writing empty space");

            listOfProcurator = new ArrayList<>();
            listOfProcurator.add(new Procurator("No Procurator data for:", ssnValue));
        }

        return new SsnData(ssnValue, listOfProcurator);
    }

    private List<Procurator> findAndCollectProcuratorBySsn(String ssnValue) throws NoSuchElementException, TimeoutException {
        getStepsManager().getNavigationSteps().enterRegistryOfCompanies(ssnValue);

        return findAndCollectProcuratorData();
    }

    private List<Procurator> findAndCollectProcuratorData() throws NoSuchElementException, TimeoutException {
        List<List<WebElement>> listOfProcuratorData = extractProcuratorData();

        return DataCollectUtil.collectProcuratorsData(listOfProcuratorData);
    }

    private List<List<WebElement>> extractProcuratorData() throws NoSuchElementException, TimeoutException {
        List<WebElement> procuratorRows = getStepsManager().getPageSearchSteps().findProcuratorRows();

        return getStepsManager().getDataSearchSteps().findProcuratorFirstCellData(procuratorRows);
    }
}
