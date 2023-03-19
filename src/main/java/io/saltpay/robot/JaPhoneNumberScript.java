package io.saltpay.robot;

import io.saltpay.models.ProcuratorPhones;
import io.saltpay.models.SsnData;
import io.saltpay.scrapper.JaPhoneNumberScrapper;
import io.saltpay.storage.FileStorageController;
import io.saltpay.utils.SaltLogger;

import java.util.ArrayList;
import java.util.List;

public class JaPhoneNumberScript {
    private static final String TAG = JaPhoneNumberScript.class.getName();
    private final JaPhoneNumberScrapper jaPhoneNumberScrapper;
    private final List<SsnData> ssnDataList;
    private final FileStorageController phoneStorage;
    private final List<ProcuratorPhones> listOfProcuratorPhones = new ArrayList<>();

    public JaPhoneNumberScript(
            JaPhoneNumberScrapper jaPhoneNumberScrapper,
            List<SsnData> ssnDataList,
            FileStorageController phoneStorage
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
                ssnData.listOfProcurator().forEach(procurator -> {
                    ProcuratorPhones phoneNumbers = jaPhoneNumberScrapper.findAndCollectDataByFullName(procurator.fullName());

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
