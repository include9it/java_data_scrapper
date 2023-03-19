package io.saltpay.robot;

import io.saltpay.models.Procurator;
import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.scripts.JaPhoneScrapperScript;
import io.saltpay.scripts.JaPhoneStartScript;
import io.saltpay.scripts.ScrapperScriptController;
import io.saltpay.storage.FileStorageController;
import io.saltpay.support.Driver;
import io.saltpay.data.JaPhoneProcuratorPhoneManager;
import io.saltpay.utils.SaltLogger;

import java.io.IOException;
import java.util.ArrayList;
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
        List<SsnData> ssnDataList = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ssnStorage, fileStorageController);

        if (ssnDataList == null) {
            SaltLogger.basic("SSN model doesn't exist! -> Exit");

            return;
        }

        List<Procurator> listOfProcurator = new ArrayList<>();

        ssnDataList.forEach(ssnData -> listOfProcurator.addAll(ssnData.listOfProcurator()));

        // Start data collection process
        ScrapperScriptController<Procurator, ProcuratorPhones> jaPhoneScriptController = new ScrapperScriptController<>(
                new JaPhoneStartScript(driver),
                new JaPhoneScrapperScript(driver),
                fileStorageController,
                listOfProcurator
        );
        jaPhoneScriptController.start(Procurator::fullName);

        // Prepare Excel file
        List<ProcuratorPhones> savedProcuratorPhones = fileStorageController.readData();
        SaltLogger.basic("savedProcuratorPhones size: " + savedProcuratorPhones.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedProcuratorPhones);
    }

    @Override
    public void turboCollect() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<SsnData> listOfSsnData = jaPhoneProcuratorPhoneManager.preparePhonesStartData(ssnStorage, fileStorageController);

        // Start multi thread collecting info process
        List<ProcuratorPhones> multiThreadSsnDataList = ThreadBot.start(
                THREADS,
                listOfSsnData,
                new JaPhoneStartScript(driver),
                new JaPhoneScrapperScript(driver)
        );
        fileStorageController.saveData(multiThreadSsnDataList);

        // Prepare Excel file
        List<ProcuratorPhones> savedThreadPhonesData = fileStorageController.readData();
        SaltLogger.basic("savedThreadPhonesData size: " + savedThreadPhonesData.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneData(savedThreadPhonesData);
    }

    public void turboCollectV2() throws IOException {
        // Prepare list of input SSN numbers for data collection
        List<SsnData> listOfSsnData = jaPhoneProcuratorPhoneManager.preparePhonesStartDataSoleTrader();

        // Start multi thread collecting info process
        List<ProcuratorPhones> multiThreadSsnDataList = ThreadBot.start(
                THREADS,
                listOfSsnData,
                new JaPhoneStartScript(driver),
                new JaPhoneScrapperScript(driver)
        );
        traderPhoneStorage.saveData(multiThreadSsnDataList);

        // Prepare Excel file
        List<ProcuratorPhones> savedThreadPhonesData = traderPhoneStorage.readData();
        SaltLogger.basic("savedThreadPhonesData size: " + savedThreadPhonesData.size());
        jaPhoneProcuratorPhoneManager.prepareExcelWithProcuratorPhoneDataSoleTrader(savedThreadPhonesData);
    }
}
