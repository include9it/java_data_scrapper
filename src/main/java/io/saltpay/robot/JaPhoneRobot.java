package io.saltpay.robot;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.scripts.JaPhoneScriptController;
import io.saltpay.storage.FileStorageController;
import io.saltpay.support.Driver;
import io.saltpay.data.JaPhoneProcuratorPhoneManager;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class JaPhoneRobot extends ScrapperRobot {
    private final FileStorageController ssnStorage = new FileStorageController(CREDIT_INFO_BACKUP_FILE);
    private final FileStorageController traderPhoneStorage = new FileStorageController(JA_PHONE_TRADER_BACKUP_FILE);
    private final JaPhoneProcuratorPhoneManager jaPhoneProcuratorPhoneManager = new JaPhoneProcuratorPhoneManager();

    public JaPhoneRobot(Driver driver) {
        super(driver, new FileStorageController(JA_PHONE_BACKUP_FILE));
    }

    @Override
    public void basicCollect() throws IOException {
        // Prepare list of input Procurator names for data collection
        List<SsnData> ssnDataList = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ssnStorage, (FileStorageController) fileStorage);

        if (ssnDataList == null) {
            SaltLogger.basic("SSN model doesn't exist! -> Exit");

            return;
        }

        // Start data collection process
        JaPhoneScriptController jaPhoneScriptController = new JaPhoneScriptController(
                driver,
                ssnDataList,
                (FileStorageController) fileStorage
        );
//        jaPhoneNumberDataCollector.start();

        // Prepare Excel file
        List<ProcuratorPhones> savedProcuratorPhones = ((FileStorageController) fileStorage).readData();
        SaltLogger.basic("savedProcuratorPhones size: " + savedProcuratorPhones.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedProcuratorPhones);
    }

    @Override
    public void turboCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<SsnData> listOfSsnData = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ssnStorage, (FileStorageController) fileStorage);

        // Start multi thread collecting info process
        List<ProcuratorPhones> multiThreadSsnDataList = JaPhoneThreadBot.start(THREADS, listOfSsnData, driver);
        ((FileStorageController) fileStorage).saveData(multiThreadSsnDataList);

        // Prepare Excel file
        List<ProcuratorPhones> savedThreadPhonesData = ((FileStorageController) fileStorage).readData();
        SaltLogger.basic("savedThreadPhonesData size: " + savedThreadPhonesData.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedThreadPhonesData);
    }

    public void turboCollectV2() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<SsnData> listOfSsnData = jaPhoneProcuratorPhoneManager.preparePhonesStartDataSoleTrader();

        // Start multi thread collecting info process
        List<ProcuratorPhones> multiThreadSsnDataList = JaPhoneThreadBot.start(THREADS, listOfSsnData, driver);
        traderPhoneStorage.saveData(multiThreadSsnDataList);

        // Prepare Excel file
        List<ProcuratorPhones> savedThreadPhonesData = traderPhoneStorage.readData();
        SaltLogger.basic("savedThreadPhonesData size: " + savedThreadPhonesData.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneDataSoleTrader(savedThreadPhonesData);
    }
}
