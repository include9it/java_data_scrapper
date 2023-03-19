package io.saltpay.robot;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.scrapper.JaPhoneNumberScrapper;
import io.saltpay.storage.FileStorageController;
import io.saltpay.support.Driver;
import io.saltpay.storage.JaPhoneProcuratorPhoneManager;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.List;

import static io.saltpay.utils.Constants.*;

public class JaPhoneRobot {
    private final Driver driver;
    private final FileStorageController ssnStorage = new FileStorageController(CREDIT_INFO_BACKUP_FILE);
    private final FileStorageController procuratorPhoneStorage = new FileStorageController(JA_PHONE_BACKUP_FILE);
    private final FileStorageController traderPhoneStorage = new FileStorageController(JA_PHONE_TRADER_BACKUP_FILE);
    private final JaPhoneProcuratorPhoneManager jaPhoneProcuratorPhoneManager = new JaPhoneProcuratorPhoneManager();

    public JaPhoneRobot(Driver driver) {
        this.driver = driver;
    }

    public void basicCollect() throws IOException {
        // Prepare list of input Procurator names for data collection
        List<SsnData> ssnDataList = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ssnStorage, procuratorPhoneStorage);

        if (ssnDataList == null) {
            SaltLogger.basic("SSN model doesn't exist! -> Exit");

            return;
        }

        // Start data collection process
        JaPhoneNumberScrapper jaPhoneNumberScrapper = new JaPhoneNumberScrapper(driver);
        JaPhoneNumberDataCollector jaPhoneNumberDataCollector = new JaPhoneNumberDataCollector(
                jaPhoneNumberScrapper,
                ssnDataList,
                procuratorPhoneStorage
        );
//        jaPhoneNumberDataCollector.start();

        // Prepare Excel file
        List<ProcuratorPhones> savedProcuratorPhones = procuratorPhoneStorage.readData();
        SaltLogger.basic("savedProcuratorPhones size: " + savedProcuratorPhones.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedProcuratorPhones);
    }

    public void multiThreadCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<SsnData> listOfSsnData = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ssnStorage, procuratorPhoneStorage);

        // Start multi thread collecting info process
        List<ProcuratorPhones> multiThreadSsnDataList = JaPhoneThreadBot.start(THREADS, listOfSsnData, driver);
        procuratorPhoneStorage.saveData(multiThreadSsnDataList);

        // Prepare Excel file
        List<ProcuratorPhones> savedThreadPhonesData = procuratorPhoneStorage.readData();
        SaltLogger.basic("savedThreadPhonesData size: " + savedThreadPhonesData.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedThreadPhonesData);
    }

    public void multiThreadCollectV2() throws IOException {
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
