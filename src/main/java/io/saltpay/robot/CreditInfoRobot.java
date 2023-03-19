package io.saltpay.robot;

import io.saltpay.models.SsnData;
import io.saltpay.scripts.CreditInfoScrapperScript;
import io.saltpay.scripts.CreditInfoStartScript;
import io.saltpay.scripts.ScrapperScriptController;
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
        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData(fileStorageController);

        // Start data collection process
        ScrapperScriptController<String, SsnData> creditInfoScriptController = new ScrapperScriptController<>(
                new CreditInfoStartScript(driver),
                new CreditInfoScrapperScript(driver),
                fileStorageController,
                listOfSsn
        );
        // 15 requests per 1 min // For 4492 records will be approximately 4 hours, 59 minutes
//        creditInfoScriptController.start(String::valueOf);

        // Prepare Excel file
        List<SsnData> savedSsnData = fileStorageController.readData();
        SaltLogger.basic("savedSsnData size: " + savedSsnData.size());
        creditInfoSsnManager.prepareExcelWithSsnData(savedSsnData);
    }

    @Override
    public void turboCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<String> listOfSsn = creditInfoSsnManager.prepareSsnStartData(fileStorageController);

        // Start multi thread collecting info process
        List<SsnData> multiThreadSsnDataList = ThreadBot.start(
                THREADS,
                listOfSsn,
                new CreditInfoStartScript(driver),
                new CreditInfoScrapperScript(driver)
        );
        fileStorageController.saveData(multiThreadSsnDataList);

        // Prepare Excel file
        List<SsnData> savedThreadSsnData = fileStorageController.readData();
        SaltLogger.basic("savedSsnThreadData size: " + savedThreadSsnData.size());
        creditInfoSsnManager.prepareExcelWithSsnData(savedThreadSsnData);
    }
}
