package io.saltpay.robot;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.scrapper.JaPhoneNumberScrapper;
import io.saltpay.storage.StorageController;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

public class JaPhoneNumberDataCollector {
    private static final String TAG = JaPhoneNumberDataCollector.class.getName();
    private final JaPhoneNumberScrapper jaPhoneNumberScrapper;
    private final List<SsnData> ssnDataList;
    private final StorageController phoneStorage;
    private final List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

    public JaPhoneNumberDataCollector(
            JaPhoneNumberScrapper jaPhoneNumberScrapper,
            List<SsnData> ssnDataList,
            StorageController phoneStorage
    ) {
        this.jaPhoneNumberScrapper = jaPhoneNumberScrapper;
        this.ssnDataList = ssnDataList;
        this.phoneStorage = phoneStorage;
    }

    public void start() {
        SaltLogger.i(TAG, "Ja Phone Bot started!");

        jaPhoneNumberScrapper.enterWebsite();

        // Get phone numbers by Procurator
        ssnDataList.forEach(ssnData ->
                ssnData.getListOfProcurator().forEach(procurator -> {
                    ProcuratorPhones phoneNumbers = jaPhoneNumberScrapper.findAndCollectDataByFullName(procurator.getFullName());

                    phoneStorage.saveData(phoneNumbers);

                    listOfProcuratorPhones.add(phoneNumbers);
                })
        );

        jaPhoneNumberScrapper.finish();
    }

    public List<ProcuratorPhones> getListOfProcuratorPhones() {
        return listOfProcuratorPhones;
    }
}
