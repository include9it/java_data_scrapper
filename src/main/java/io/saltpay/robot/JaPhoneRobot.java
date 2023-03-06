package io.saltpay.robot;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.scrapper.JaPhoneNumberScrapper;
import io.saltpay.support.DriverManager;
import io.saltpay.storage.CreditInfoStorage;
import io.saltpay.utils.JaPhoneProcuratorPhoneManager;
import io.saltpay.storage.JaPhoneStorage;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.List;

import static io.saltpay.utils.Constants.JA_PHONE_BACKUP_FILE;
import static io.saltpay.utils.Constants.THREADS;

public class JaPhoneRobot {
    private final DriverManager driverManager;
    private final CreditInfoStorage ciSaveManager = new CreditInfoStorage();
    private final JaPhoneStorage jaPhoneStorage = new JaPhoneStorage();
    private final JaPhoneProcuratorPhoneManager jaPhoneProcuratorPhoneManager = new JaPhoneProcuratorPhoneManager();

    public JaPhoneRobot(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    public void basicCollect() throws IOException {
        // Prepare list of input Procurator names for data collection
        List<SsnData> ssnDataList = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ciSaveManager, jaPhoneStorage);

        if (ssnDataList == null) {
            SaltLogger.basic("SSN model doesn't exist! -> Exit");

            return;
        }

        // Start data collection process
        JaPhoneNumberScrapper jaPhoneNumberScrapper = new JaPhoneNumberScrapper(driverManager);
        JaPhoneNumberDataCollector jaPhoneNumberDataCollector = new JaPhoneNumberDataCollector(
                jaPhoneNumberScrapper,
                ssnDataList,
                jaPhoneStorage
        );
        jaPhoneNumberDataCollector.start();

        // Prepare Excel file
        List<ProcuratorPhones> savedProcuratorPhones = jaPhoneStorage.readSavedPhonesData(JA_PHONE_BACKUP_FILE);
        SaltLogger.basic("savedProcuratorPhones size: " + savedProcuratorPhones.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedProcuratorPhones);
    }

    public void multiThreadCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<SsnData> listOfSsnData = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ciSaveManager, jaPhoneStorage);

        // Start multi thread collecting info process
        List<ProcuratorPhones> multiThreadSsnDataList = JaPhoneThreadBot.start(THREADS, listOfSsnData, driverManager);
        jaPhoneStorage.saveProcuratorPhoneData(JA_PHONE_BACKUP_FILE, multiThreadSsnDataList, false);

        // Prepare Excel file
        List<ProcuratorPhones> savedThreadPhonesData = jaPhoneStorage.readSavedPhonesData(JA_PHONE_BACKUP_FILE);
        SaltLogger.basic("savedThreadPhonesData size: " + savedThreadPhonesData.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedThreadPhonesData);
    }
}
