package io.saltpay.robot;

import io.saltpay.models.SsnData;
import io.saltpay.scripts.CreditInfoScriptController;
import io.saltpay.storage.FileStorageController;
import io.saltpay.support.Driver;
import io.saltpay.data.CreditInfoSsnManager;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.List;

import static io.saltpay.utils.Constants.CREDIT_INFO_BACKUP_FILE;
import static io.saltpay.utils.Constants.THREADS;

public class CreditInfoRobot extends ScrapperRobot {
    private final CreditInfoSsnManager creditInfoSsnManager = new CreditInfoSsnManager();

    public CreditInfoRobot(Driver driver) {
        super(driver, new FileStorageController(CREDIT_INFO_BACKUP_FILE));
    }

    @Override
    public void basicCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData((FileStorageController) fileStorage);

        // Start data collection process
        CreditInfoScriptController creditInfoScriptController = new CreditInfoScriptController(
                driver,
                listOfSsn,
                (FileStorageController) fileStorage
        );
        // 15 requests per 1 min // For 4492 records will be approximately 4 hours, 59 minutes
//        creditInfoScriptWrapper.start();

        // Prepare Excel file
        List<SsnData> savedSsnData = ((FileStorageController) fileStorage).readData();
        SaltLogger.basic("savedSsnData size: " + savedSsnData.size());
//        creditInfoSsnManager.prepareExcelWithSsnData(savedSsnData);
    }

    @Override
    public void turboCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData((FileStorageController) fileStorage);

        // Start multi thread collecting info process
        List<SsnData> multiThreadSsnDataList = CreditInfoThreadBot.start(THREADS, listOfSsn, driver);
        ((FileStorageController) fileStorage).saveData(multiThreadSsnDataList);

        // Prepare Excel file
        List<SsnData> savedThreadSsnData = ((FileStorageController) fileStorage).readData();
        SaltLogger.basic("savedSsnThreadData size: " + savedThreadSsnData.size());
        creditInfoSsnManager.prepareExcelWithSsnData(savedThreadSsnData);
    }
}
