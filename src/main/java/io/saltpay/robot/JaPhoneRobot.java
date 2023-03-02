package io.saltpay.robot;

import io.saltpay.model.ProcuratorPhones;
import io.saltpay.model.SsnData;
import io.saltpay.scrapper.JaPhoneNumberScrapper;
import io.saltpay.support.DriverManager;
import io.saltpay.storage.CreditInfoSaveManager;
import io.saltpay.utils.JaPhoneProcuratorPhoneManager;
import io.saltpay.storage.JaPhoneSaveManager;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.List;

import static io.saltpay.utils.Constants.JA_PHONE_BACKUP_FILE;

public class JaPhoneRobot {
    private final DriverManager driverManager;
    private final CreditInfoSaveManager ciSaveManager = new CreditInfoSaveManager();
    private final JaPhoneSaveManager jaPhoneSaveManager = new JaPhoneSaveManager();
    private final JaPhoneProcuratorPhoneManager jaPhoneProcuratorPhoneManager = new JaPhoneProcuratorPhoneManager();

    public JaPhoneRobot(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    public void basicCollect() throws IOException {
        // Prepare list of input Procurator names for data collection
        List<SsnData> ssnDataList = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ciSaveManager, jaPhoneSaveManager);

        if (ssnDataList == null) {
            SaltLogger.basic("SSN model doesn't exist! -> Exit");

            return;
        }

        // Start data collection process
        JaPhoneNumberScrapper jaPhoneNumberScrapper = new JaPhoneNumberScrapper(driverManager);
        JaPhoneNumberDataCollector jaPhoneNumberDataCollector = new JaPhoneNumberDataCollector(
                jaPhoneNumberScrapper,
                ssnDataList,
                jaPhoneSaveManager
        );
        jaPhoneNumberDataCollector.start();

        // Prepare Excel file
        List<ProcuratorPhones> savedProcuratorPhones = jaPhoneSaveManager.readSavedPhonesData(JA_PHONE_BACKUP_FILE);
        SaltLogger.basic("savedProcuratorPhones size: " + savedProcuratorPhones.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedProcuratorPhones);
    }

    public void multiThreadCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<SsnData> listOfSsnData = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ciSaveManager, jaPhoneSaveManager);

        // Start multi thread collecting info process
        List<ProcuratorPhones> multiThreadSsnDataList = JaPhoneThreadBot.start(10, listOfSsnData, driverManager);
        jaPhoneSaveManager.saveProcuratorPhoneData(JA_PHONE_BACKUP_FILE, multiThreadSsnDataList);

        // Prepare Excel file
        List<ProcuratorPhones> savedThreadPhonesData = jaPhoneSaveManager.readSavedPhonesData(JA_PHONE_BACKUP_FILE);
        SaltLogger.basic("savedThreadPhonesData size: " + savedThreadPhonesData.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedThreadPhonesData);
    }
}
