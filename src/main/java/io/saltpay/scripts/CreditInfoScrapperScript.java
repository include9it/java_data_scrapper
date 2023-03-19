package io.saltpay.scripts;

import io.saltpay.models.Procurator;
import io.saltpay.models.SsnData;
import io.saltpay.support.Driver;
import io.saltpay.utils.DataCollectUtil;
import io.saltpay.utils.SaltLogger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CreditInfoScrapperScript extends Script implements ScrapperScript<SsnData> {
    private static final String TAG = CreditInfoScrapperScript.class.getName();

    public CreditInfoScrapperScript(Driver driver) {
        super(driver);
    }

    @Override
    public SsnData findAndCollectDataByValue(String ssnValue) {
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
