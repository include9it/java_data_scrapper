package io.saltpay.scrapper;

import io.saltpay.model.Procurator;
import io.saltpay.model.SsnData;
import io.saltpay.model.excel.ExcelData;
import io.saltpay.model.excel.SheetData;
import io.saltpay.steps.StepsManager;
import io.saltpay.utils.CreditInfoSaveManager;
import io.saltpay.utils.DataCollectUtil;
import io.saltpay.utils.ExcelManager;
import io.saltpay.utils.SaltLogger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class CreditInfo extends Scrapper {
    private static final String TAG = CreditInfo.class.getName();
    private final CreditInfoSaveManager ciSaveManager;

    public CreditInfo(StepsManager stepsManager, CreditInfoSaveManager ciSaveManager) {
        super(stepsManager);

        this.ciSaveManager = ciSaveManager;
    }

    @Override
    public void start() throws IOException {
        ExcelManager excelManager = new ExcelManager();
        // Get list of SSN values
        List<String> listOfSsn = excelManager.getFirstColumnData();

        List<SsnData> savedSsnList = ciSaveManager.readSavedSsnData();

        if (savedSsnList != null) {
            int lastEntryIndex = savedSsnList.size() - 1;

            SsnData lastSsnEntry = savedSsnList.get(lastEntryIndex);

            int sizeOfSsnList = listOfSsn.size();
            int cutIndex = listOfSsn.indexOf(lastSsnEntry.getSsnValue());

            listOfSsn = listOfSsn.subList(cutIndex, sizeOfSsnList);
        }

        enterAndLogin();
        changeLocale();

        // Get Procurators data by SSN number
        List<SsnData> listOfSsnData = new ArrayList<>();
        listOfSsn.forEach(ssn -> {
            SsnData ssnData = findAndCollectDataBySsn(ssn);

            ciSaveManager.saveSsnData(ssnData);

            listOfSsnData.add(ssnData);
        });

        SheetData ssnSheet = DataCollectUtil.collectSheetData(listOfSsnData);

        List<SheetData> dataSheets = new ArrayList<>();
        dataSheets.add(ssnSheet);

        // Create new doc with target info
        ExcelData excelData = new ExcelData(RESOURCE_FILE_PATH + CREDIT_INFO_WRITE_FILE, dataSheets);
        excelManager.writeExcel(excelData);
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
