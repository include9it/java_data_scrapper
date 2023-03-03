package io.saltpay.robot;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.scrapper.JaPhoneNumberScrapper;
import io.saltpay.storage.JaPhoneStorage;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

import static io.saltpay.utils.Constants.JA_PHONE_BACKUP_FILE;

public class JaPhoneNumberDataCollector {
    private static final String TAG = JaPhoneNumberDataCollector.class.getName();
    private final JaPhoneNumberScrapper jaPhoneNumberScrapper;
    private final List<SsnData> ssnDataList;
    private final JaPhoneStorage jaPhoneStorage;
    private final List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

    public JaPhoneNumberDataCollector(JaPhoneNumberScrapper jaPhoneNumberScrapper, List<SsnData> ssnDataList, JaPhoneStorage jaPhoneStorage) {
        this.jaPhoneNumberScrapper = jaPhoneNumberScrapper;
        this.ssnDataList = ssnDataList;
        this.jaPhoneStorage = jaPhoneStorage;
    }

    public void start() {
        SaltLogger.i(TAG, "Ja Phone Bot started!");

        jaPhoneNumberScrapper.enterWebsite();

        // Get phone numbers by Procurator
        ssnDataList.forEach(ssnData ->
                ssnData.getListOfProcurator().forEach(procurator -> {
                    ProcuratorPhones phoneNumbers = jaPhoneNumberScrapper.findAndCollectDataByFullName(procurator.getFullName());

                    jaPhoneStorage.saveProcuratorPhoneData(JA_PHONE_BACKUP_FILE, phoneNumbers);

                    listOfProcuratorPhones.add(phoneNumbers);
                })
        );

        jaPhoneNumberScrapper.finish();
    }

    public List<ProcuratorPhones> getListOfProcuratorPhones() {
        return listOfProcuratorPhones;
    }
}
